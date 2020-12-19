package sintatico;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import geracaodecodigo.GeradorDeCodigo;
import lexico.AnalisadorLexico;
import lexico.Token;
import posfixo.PosFixo;
import tabeladesimbolos.Funcao;
import tabeladesimbolos.Procedimento;
import tabeladesimbolos.Simbolo;
import tabeladesimbolos.TabelaDeSimbolos;
import tabeladesimbolos.Variavel;
import utils.Simbolos;
import utils.SimbolosPosFixo;

public class AnalisadorSintatico {

	private AnalisadorLexico lexico;
	private Token token;
	private TabelaDeSimbolos tabelaSimbolos;
	private Integer escopo;
	private PosFixo expressaoPosFixa;
	private GeradorDeCodigo geradorCodigo;
	private int labelCorrente;
	private Integer ultimoM;
	private Integer memoria;
	private Map<String, String> funcaoLabel;
	private boolean retornoFuncao;
	private String caminhoArquivo;
	private List<String> nomesFuncaoProcedimento;

	
	/**
	 * 
	 * @category Analisador Sintatico
	 * @author Thales e Elton
	 */
	public AnalisadorSintatico(String caminhoArquivo) throws Exception {
		iniciaAtributos(caminhoArquivo);
		analise();
	}

	
	/***
	 * @category Métodos auxiliares do compilador
	 * @author Thales e Elton
	 * 
	 */
	private void iniciaAtributos(String caminhoArquivo) throws IOException{
		this.lexico = new AnalisadorLexico(caminhoArquivo);
		this.caminhoArquivo = getCaminhoTotalArquivoObjeto(caminhoArquivo);
		this.geradorCodigo = new GeradorDeCodigo(this.caminhoArquivo);
		this.tabelaSimbolos = new TabelaDeSimbolos();
		this.expressaoPosFixa = new PosFixo();
		this.labelCorrente = 0;
		this.memoria = -1;
		this.funcaoLabel = new HashMap<>();
		this.retornoFuncao = false;
		this.ultimoM = 0;
		this.escopo = 0;
		this.nomesFuncaoProcedimento = new ArrayList<>();
	}
	
	private void nextToken() throws Exception {
		this.token = lexico.getToken();

		if (token != null) {
			if (this.token.getIdSimbolo() == 40) {
				erroLexico(this.token);
			}
		}
	}
	
	private String getCaminhoTotalArquivoObjeto (String path)
	{
		int i;
		String pathGeracao = path;
		for (i=pathGeracao.length()-1;i>0;i--)
		{
			if(pathGeracao.charAt(i) == '.')
			{
				break;
			}
		}
		pathGeracao = (String) pathGeracao.substring(0, i);
		pathGeracao = pathGeracao.concat(".o");
		return pathGeracao;
	}
	
	/**
	 * @category Métodos para lançamento de erros
	 * @author Thales e Elton
	 */
	public void erroTabelaDeSimbolos(String simbolo) throws Exception {
		// erro Tabela de simbolo
		throw new Exception("Erro tabela de simbolos -> " + simbolo);
	}

	public void erroSemantico(String simbolo) throws Exception {
		throw new Exception("Erro semântico -> " + simbolo);
	}
	
	public void erroSintatico(Token token, String simbolo) throws Exception {
		throw new Exception("Erro sintatico -> Linha:" + this.token.getLinha() + " Coluna:"
				+ this.token.getColunaPrimeiroCaracter() + " Erro presente na palavra: '" + this.token.getLexema()
				+ "'");
	}
	
	public void erroLexico(Token token) throws Exception{
		throw new Exception("Erro lexico ->" + this.token.getdescricaoErro());
	}
	
	/***
	 * @category Métodos para analise
	 * @author Thales e Elton
	 * 
	 */
	private void analise() throws Exception{
		nextToken();
		while (this.token != null) {
			if (Simbolos.isSPrograma(this.token.getIdSimbolo())) {
				geradorCodigo.escreveComando("START");
				nextToken();
				if (Simbolos.isSIdentificador(this.token.getIdSimbolo())) {
					// insere_tabela(this.token.lexema,"nomedeprograma","","")
					Procedimento proc = new Procedimento();
					proc.setLexema(this.token.getLexema());
					proc.setEscopo(this.escopo);
					this.escopo++;
					tabelaSimbolos.inserirTabela(proc);

					nextToken();
					// ==spontoevirgula
					if (Simbolos.isSPontoEVirgula(this.token.getIdSimbolo())) {
						analisaBloco(false);
						// ==sponto
						if (this.token != null) {
							if (Simbolos.isSPonto(this.token.getIdSimbolo())) {
								nextToken();
								geradorCodigo.escreveComando("HLT");
								if (this.token == null) {
									System.out.println("Sucesso");
								} else {
									throw new Exception("Erro após fim do arquivo");
								}
							} else {
								erroSintatico(this.token, "ponto");
							}
						} else {
							throw new Exception("Programa sem ponto final");
						}
					} else {
						erroSintatico(this.token, "ponto e virgula");
					}
				} else {
					erroSintatico(this.token, "identificador");
				}
			} else {
				erroSintatico(this.token, "programa");
			}
		}
		tabelaSimbolos.retiraEscopo();
		geradorCodigo.fechaArquivo();
		throw new Exception("Compilação sem erros");
	}
	
	private void analisaBloco(boolean isFunc) throws Exception {
		boolean enableDalloc = false;
		nextToken();
		ParametrosMN mn = new ParametrosMN(ultimoM, 0);
		if (Simbolos.isSVar(this.token.getIdSimbolo())) {
			analisaEtapaDeclaracaoVariaveis(mn);
			geradorCodigo.escreveComando("alloc " + mn.getM() + "," + mn.getN());
			enableDalloc = true;
		}
		analisaSubrotinas();
		analisaComandos(isFunc, mn);

		if (enableDalloc && !isFunc) {
			geradorCodigo.escreveComando("dalloc " + mn.getM() + "," + mn.getN());
		}
		memoria = memoria - mn.getN();
		ultimoM = ultimoM - mn.getN();
	}

	private void analisaEtapaDeclaracaoVariaveis(ParametrosMN mn) throws Exception {
		// compara com svar
		if (Simbolos.isSVar(this.token.getIdSimbolo())) {
			nextToken();
			// compara com sidentificador
			if (Simbolos.isSIdentificador(this.token.getIdSimbolo())) {
				// sidentificador
				while (Simbolos.isSIdentificador(this.token.getIdSimbolo())) {
					analisaVariaveis(mn);
					// spontoevirgula
					if (Simbolos.isSPontoEVirgula(this.token.getIdSimbolo())) {
						nextToken();
					} else {
						erroSintatico(this.token, "ponto e virgula");
					}
				}
			} else {
				erroSintatico(this.token, "identificador");
			}
		} else {
			erroSintatico(this.token, "var");
		}

	}

	public void analisaVariaveis(ParametrosMN mn) throws Exception {
		// até ser igual a dois pontos
		while (!Simbolos.isSDoisPontos(this.token.getIdSimbolo())) {
			// se for identificador
			if (Simbolos.isSIdentificador(this.token.getIdSimbolo())) {

				/*
				 * Pesquisa_duplicvar_ tabela(this.token.lexema) se nao
				 * encontrou duplicidade
				 */
				if (tabelaSimbolos.procuraDuplicidadeNomeVariavel(this.token.getLexema()) == false) {
					Variavel var = new Variavel();
					memoria++;
					var.setLexema(this.token.getLexema());
					var.setMemoria(memoria);
					var.setTipo(null);
					mn.incrementaN();
					// insere_tabela(this.token.lexema, "variavel")
					tabelaSimbolos.inserirTabela(var);
					nextToken();
					// se for igual a virgula (27) ou dois pontos (39)
					if ((Simbolos.isSVirgula(this.token.getIdSimbolo()))
							|| (Simbolos.isSDoisPontos(this.token.getIdSimbolo()))) {
						if (Simbolos.isSVirgula(this.token.getIdSimbolo())) {
							nextToken();
							// 39 -- dois pontos
							if (Simbolos.isSDoisPontos(this.token.getIdSimbolo())) {
								erroSintatico(this.token, "É dois pontos (analiseVariaveis");
							}
						}
					} else {
						erroSintatico(this.token, "virgula ou dois pontos (AnalisaVariaveis)");
					}
				}

				else {
					erroTabelaDeSimbolos("Váriavel Repetida '" + this.token.getLexema() + "'");
				}

			} else {
				erroSintatico(this.token, "identificador");
			}

		}
		ultimoM = ultimoM + mn.getN();
		nextToken();
		analisaTipo();
	}

	public void analisaTipo() throws Exception {
		// se nao for inteiro ou booleano
		if ((!Simbolos.isSInteiro(this.token.getIdSimbolo())) && (!Simbolos.isSBooleano(this.token.getIdSimbolo()))) {
			erroSintatico(this.token, "inteiro ou booleano");
		} else {
			// senão coloca_tipo_tabela(this.token.lexema)
			tabelaSimbolos.inserirTipoVariavel(this.token.getLexema());
		}
		nextToken();
	}

	public void analisaSubrotinas() throws Exception {
		// Def. auxrot, flag inteiro
		// flag =0
		int flag = 0;
		int labelAux = labelCorrente;
		// se for procedimento ou função
		if ((Simbolos.isSProcedimento(this.token.getIdSimbolo())) || (Simbolos.isSFuncao(this.token.getIdSimbolo()))) {
			/*
			 * auxrot:= rotulo GERA(ï¿½ ï¿½,JMP,rotulo,ï¿½ ï¿½) {Salta
			 * sub-rotinas} rotulo:= rotulo + 1 flag = 1
			 */

			geradorCodigo.escreveComando("JMP L" + labelCorrente);
			labelCorrente++;
			flag = 1;
		}

		// enquanto for procedimento ou função
		while ((Simbolos.isSProcedimento(this.token.getIdSimbolo()))
				|| (Simbolos.isSFuncao(this.token.getIdSimbolo()))) {
			// se for procedimento
			if (Simbolos.isSProcedimento(this.token.getIdSimbolo())) {
				analisaDeclaracaoProcedimento();
			} else {
				analisaDeclaracaoFuncao();
			}

			// se for ponto e virgula
			if (Simbolos.isSPontoEVirgula(this.token.getIdSimbolo())) {
				nextToken();
			}

			
			/*
			 * if flag = 1 entï¿½o Gera(auxrot,NULL,ï¿½ ï¿½,ï¿½ ï¿½) {inï¿½cio
			 * do principal}
			 */
		}
		if (flag == 1) {
			geradorCodigo.escreveComando("L" + labelAux + ": NULL");
		}
	}

	public void analisaDeclaracaoProcedimento() throws Exception {

		nextToken();
		// nivel := "L" (marca ou novo galho)

		// se for identificador
		if (Simbolos.isSIdentificador(this.token.getIdSimbolo())) {
			/*
			 * pesquisa_declproc_tabela(token.lexema) se não encontrou então
			 * inicio Insere_tabela(token.lexema,"procedimento",nivel, rótulo)
			 * {guarda na TabSimb} Gera(rotulo,NULL,ï¿½ ï¿½,ï¿½ ï¿½) {CALL irï¿½
			 * buscar este rï¿½tulo na TabSimb} rotulo:= rotulo+1
			 */
			nomesFuncaoProcedimento.add(this.token.getLexema());
			if (tabelaSimbolos.procurarDeclaracaoTabela(this.token.getLexema()) == false) {
				Procedimento proc = new Procedimento();
				proc.setLexema(this.token.getLexema());
				proc.setEscopo(this.escopo);
				this.escopo++;
				tabelaSimbolos.inserirTabela(proc);
				geradorCodigo.escreveComando("L" + labelCorrente + ": NULL");
				funcaoLabel.put(this.token.getLexema(), "L" + labelCorrente);
				labelCorrente++;

				nextToken();
				// se for ponto e virgula
				if (Simbolos.isSPontoEVirgula(this.token.getIdSimbolo())) {
					analisaBloco(false);
				} else {
					erroSintatico(this.token, "ponto e virgula");
				}
			} else {
				erroTabelaDeSimbolos(
						"O procedimento '" + this.token.getLexema() + "' está sendo declarado mais de uma vez");
			}

		} else {
			erroSintatico(this.token, "identificador");
		}
		/*
		 * 
		 * fim senao ERRO DESEMPILHA OU VOLTA NIVEL
		 */
		geradorCodigo.escreveComando("RETURN");
		nomesFuncaoProcedimento.remove(nomesFuncaoProcedimento.size() - 1);
		tabelaSimbolos.retiraEscopo();

	}

	public void analisaDeclaracaoFuncao() throws Exception {

		nextToken();
		// nivel := "L" (marca ou novo galho)
		// se for identificador
		String nomeFuncao = this.token.getLexema();
		nomesFuncaoProcedimento.add(nomeFuncao);
		if (Simbolos.isSIdentificador(this.token.getIdSimbolo())) {
			/*
			 * pesquisa_declfunc_tabela(token.lexema) se nao encontrou entao
			 * inicio Insere_tabela(token.lexema,"",nivel,rotulo)
			 */
			if (tabelaSimbolos.procurarDeclaracaoTabela(this.token.getLexema()) == false) {
				Funcao func = new Funcao();
				func.setLexema(this.token.getLexema());
				func.setEscopo(this.escopo);
				this.escopo++;
				tabelaSimbolos.inserirTabela(func);
				geradorCodigo.escreveComando("L" + labelCorrente + ": NULL");
				funcaoLabel.put(this.token.getLexema(), "L" + labelCorrente);
				labelCorrente++;
				nextToken();
				if (Simbolos.isSDoisPontos(this.token.getIdSimbolo())) {
					nextToken();
					// se for inteiro ou booleano
					if ((Simbolos.isSInteiro(this.token.getIdSimbolo()))
							|| (Simbolos.isSBooleano(this.token.getIdSimbolo()))) {
						// inteiro
						if (Simbolos.isSInteiro(this.token.getIdSimbolo())) {
							/*
							 * entao TABSIMB[pc].tipo:= "funcao inteiro"
							 */
							tabelaSimbolos.inserirTipoFuncao(this.token.getLexema());
						} else {
							/*
							 * TABSIMB[pc].tipo:= "funcao boolean"
							 */
							tabelaSimbolos.inserirTipoFuncao(this.token.getLexema());
						}
						nextToken();
						// se for ponto e virgula
						if (Simbolos.isSPontoEVirgula(this.token.getIdSimbolo())) {
							analisaBloco(true);
						}
					} else {
						erroSintatico(this.token, "tipo de variavel");
					}
				} else {
					erroSintatico(this.token, "dois pontos");
				}
			} else {
				erroTabelaDeSimbolos("A função '" + this.token.getLexema() + "' não pode receber este nome: Linha:"
						+ this.token.getLinha() + " motivo: duplicidade");
			}

		} else {
			/* senao ERRO */
			erroSintatico(this.token, "identificador");
		}
		/* DESEMPILHA OU VOLTA NiVEL */
		if (!retornoFuncao) {
			erroSemantico("Não existe retorno de funcão em todas possibilidades");
		}
		retornoFuncao = false;
		nomesFuncaoProcedimento.remove(nomesFuncaoProcedimento.size() - 1);
		tabelaSimbolos.retiraEscopo();

	}

	public void analisaComandos(boolean isFunc, ParametrosMN mn) throws Exception {
		// se for inicio
		if (Simbolos.isSInicio(this.token.getIdSimbolo())) {
			nextToken();
			analisaComandoSimples(isFunc, mn);
			// ser diferente de fimthis.token = reader.getToken();
			while (!Simbolos.isSFim(this.token.getIdSimbolo())) {
				// ponto e virgula
				if (Simbolos.isSPontoEVirgula(this.token.getIdSimbolo())) {
					nextToken();
					if (!Simbolos.isSFim(this.token.getIdSimbolo())) {
						analisaComandoSimples(isFunc, mn);
					}
				} else {
					erroSintatico(this.token, "ponto e virgula");
				}
			}
			nextToken();
		} else {
			erroSintatico(this.token, "programa");
		}
	}

	public void analisaComandoSimples(boolean isFunc, ParametrosMN mn) throws Exception {

		if (!retornoFuncao) {
			if (Simbolos.isSIdentificador(this.token.getIdSimbolo())) {

				analisaAtribuicaoChamadaProcedimento(isFunc, mn);
			} else if (Simbolos.isSSe(this.token.getIdSimbolo())) {
				analisaSe(isFunc, mn);
			} else if (Simbolos.isSEnquanto(this.token.getIdSimbolo()) || Simbolos.isSFaca(this.token.getIdSimbolo())) {
				analisaEnquanto(isFunc, mn);
			} else if (Simbolos.isSLeia(this.token.getIdSimbolo())) {
				analisaLeia();
			} else if (Simbolos.isSEscreva(this.token.getIdSimbolo())) {
				analisaEscreva();
			} else {
				analisaComandos(isFunc, mn);
			}
		} else {
			erroSemantico("Código inalcançavel");
		}
	}

	public void analisaAtribuicaoChamadaProcedimento(boolean isFunc, ParametrosMN mn) throws Exception {
		Simbolo variavelAtribuicao = tabelaSimbolos.oQueEuSou(token.getLexema());

		if (variavelAtribuicao == null) {
			erroTabelaDeSimbolos("Variavel ou função inexistente ou fora de escopo");
		}

		nextToken();
		if (Simbolos.isSAtribuicao(this.token.getIdSimbolo())) {
			analisaAtribuicao(variavelAtribuicao, isFunc, mn);
		} else {
			analisaChamadaProcedimento(variavelAtribuicao);
		}
	}

	private void analisaChamadaProcedimento(Simbolo variavelAtribuicao) throws Exception {
		if (tabelaSimbolos.procurarDeclaracaoProcedimentoTabela(variavelAtribuicao.getLexema())) {
			geradorCodigo.escreveComando("CALL " + funcaoLabel.get(variavelAtribuicao.getLexema()));
		} else {
			erroSemantico("O procedimento " + variavelAtribuicao.getLexema() + " não foi declarado para este escopo");
		}
	}

	private void analisaAtribuicao(Simbolo variavelAtribuicao, boolean isFunc, ParametrosMN mn) throws Exception {
		nextToken();
		expressaoPosFixa.clear();
		analisaExpressao(variavelAtribuicao);
		expressaoPosFixa.setLista(expressaoPosFixa.getLista());
		if (variavelAtribuicao instanceof Funcao) {
			if (!variavelAtribuicao.getLexema()
					.equals(nomesFuncaoProcedimento.get(nomesFuncaoProcedimento.size() - 1))) {
				erroSemantico("Retorno da função " + nomesFuncaoProcedimento.get(nomesFuncaoProcedimento.size() - 1)
						+ " invalido");
			}

			if (isFunc) {
				if (!((Funcao) variavelAtribuicao).getTipo().equals(expressaoPosFixa.getTipoExpressao())) {
					erroSemantico(variavelAtribuicao.getLexema() + " não é do tipo " + expressaoPosFixa.getTipoExpressao());
				}
			} else {
				erroSemantico("Retorno de função fora da função");
			}
		} else {

			if (variavelAtribuicao instanceof Procedimento) {
				erroSemantico("Não é possivel atribuir um valor ao procedimento " + variavelAtribuicao.getLexema());
			} else if (!((Variavel) variavelAtribuicao).getTipo().equals(expressaoPosFixa.getTipoExpressao())) {
				erroSemantico(variavelAtribuicao.getLexema() + " não é do tipo " + expressaoPosFixa.getTipoExpressao());
			}
		}
		geradorCodigo.geraCodigoExpressao(expressaoPosFixa, tabelaSimbolos);
		if (variavelAtribuicao instanceof Variavel) {
			geradorCodigo.escreveComando("STR " + (tabelaSimbolos.oQueEuSou(variavelAtribuicao.getLexema())).getMemoria());
		} else if (variavelAtribuicao instanceof Funcao) {
			retornoFuncao = true;
			geradorCodigo.escreveComando("RETURNF " + (mn.getN() != 0 ? mn.getM() : 0) + "," + mn.getN());
		} else if (variavelAtribuicao instanceof Procedimento) {
			erroSemantico("Não é possivel atribuir um valor ao procedimento " + variavelAtribuicao.getLexema());
		}
	}

	public void analisaSe(boolean isFunc, ParametrosMN mn) throws Exception {
		boolean retornoFuncaoAux;
		int labelAux = labelCorrente;
		int otherLabelAux = 0;

		nextToken();
		expressaoPosFixa.clear();
		analisaExpressao(null);
		expressaoPosFixa.setLista(expressaoPosFixa.getLista());

		if ("inteiro".equals(expressaoPosFixa.getTipoExpressao())) {
			erroSemantico("Comando 'se' espera uma expressão booleana");
		}
		geradorCodigo.geraCodigoExpressao(expressaoPosFixa, tabelaSimbolos);
		geradorCodigo.escreveComando("JMPF L" + labelAux);
		labelCorrente++;
		// entao
		if (Simbolos.isSEntao(this.token.getIdSimbolo())) {
			nextToken();
			analisaComandoSimples(isFunc, mn);
			retornoFuncaoAux = retornoFuncao;
			if (Simbolos.isSSenao(this.token.getIdSimbolo())) {
				otherLabelAux = labelCorrente;
				labelCorrente++;
				geradorCodigo.escreveComando("JMP L" + otherLabelAux);
				geradorCodigo.escreveComando("L" + labelAux + ": NULL");
				retornoFuncao = false;
				nextToken();
				analisaComandoSimples(isFunc, mn);
				geradorCodigo.escreveComando("L" + otherLabelAux + ": NULL");
				retornoFuncao = retornoFuncao & retornoFuncaoAux;
			} else {
				geradorCodigo.escreveComando("L" + labelAux + ": NULL");
				retornoFuncao = false;
			}
		} else {
			erroSintatico(this.token, "entao");
		}
	}

	public void analisaExpressao(Simbolo variavelAtribuicao) throws Exception {
		analisaExpressaoSimples(variavelAtribuicao);
		// é smaior ou smaiorig ou sig ou smenor ou smenorig ou sdif
		if ((Simbolos.isSMaior(this.token.getIdSimbolo())) || (Simbolos.isSMaiorIgual(this.token.getIdSimbolo()))
				|| (Simbolos.isSIgual(this.token.getIdSimbolo())) || (Simbolos.isSMenor(this.token.getIdSimbolo()))
				|| (Simbolos.isSMenorIgual(this.token.getIdSimbolo()))
				|| (Simbolos.isSDiferente(this.token.getIdSimbolo()))) {
			expressaoPosFixa.construirPosFixa(this.token.getLexema(), "comparacao");
			nextToken();
			analisaExpressaoSimples(variavelAtribuicao);

		}
	}

	public void analisaExpressaoSimples(Simbolo variavelAtribuicao) throws Exception {
		// é mais ou menos
		if ((Simbolos.isSMais(this.token.getIdSimbolo())) || (Simbolos.isSMenos(this.token.getIdSimbolo()))) {
			expressaoPosFixa.construirPosFixa(this.token.getLexema(), SimbolosPosFixo.UNARIO);
			nextToken();
		}

		analisaTermo(variavelAtribuicao);
		// e mais ou menos ou ou
		while ((Simbolos.isSMais(this.token.getIdSimbolo())) || (Simbolos.isSMenos(this.token.getIdSimbolo()))
				|| (Simbolos.isSOu(this.token.getIdSimbolo()))) {
			if (this.token.getLexema().equals("ou")) {
				expressaoPosFixa.construirPosFixa(this.token.getLexema(), "logico");
			} else {
				expressaoPosFixa.construirPosFixa(this.token.getLexema(), "somaSub");
			}
			nextToken();
			analisaTermo(variavelAtribuicao);
		}

	}

	public void analisaTermo(Simbolo variavelAtribuicao) throws Exception {
		analisaFator(variavelAtribuicao);
		// multplicacao/se/divisao
		while ((Simbolos.isSMultiplicacao(this.token.getIdSimbolo()))
				|| (Simbolos.isSE(this.token.getIdSimbolo()) || (Simbolos.isSDivisao(this.token.getIdSimbolo())))) {
			if (Simbolos.isSMultiplicacao(this.token.getIdSimbolo())
					|| Simbolos.isSDivisao(this.token.getIdSimbolo())) {
				expressaoPosFixa.construirPosFixa(this.token.getLexema(), "multDiv");
			} else {
				expressaoPosFixa.construirPosFixa(this.token.getLexema(), "logico");
			}
			nextToken();
			analisaFator(variavelAtribuicao);
		}
	}

	public void analisaFator(Simbolo variavelAtribuicao) throws Exception {
		// identificador (*Variavel ou função)
		if (Simbolos.isSIdentificador(this.token.getIdSimbolo())) {
			if (tabelaSimbolos.procurarDeclaracaoTabela(this.token.getLexema())) {
				if (tabelaSimbolos.procurarDeclaracaoFuncaoTabela(this.token.getLexema())) {
					expressaoPosFixa.construirPosFixa(this.token.getLexema(),
							tabelaSimbolos.procuraTipoFuncao(this.token.getLexema()));
					analisaChamadaFuncao(variavelAtribuicao);
				} else {
					// é variavel
					if (tabelaSimbolos.oQueEuSou(this.token.getLexema()) instanceof Procedimento) {
						erroSemantico("O procedimento " + this.token.getLexema() + " não tem retorno");
					}
					expressaoPosFixa.construirPosFixa(this.token.getLexema(),
							tabelaSimbolos.procuraTipoVariavel(this.token.getLexema()));
					nextToken();
				}

			} else {
				erroTabelaDeSimbolos("Não existe esse identificador '" + this.token.getLexema() + "' para esse escopo");
			}

		} else if (Simbolos.isSNumero(this.token.getIdSimbolo())) {
			expressaoPosFixa.construirPosFixa(this.token.getLexema(), "numero");
			nextToken();
		} else if (Simbolos.isSNao(this.token.getIdSimbolo())) {
			expressaoPosFixa.construirPosFixa(this.token.getLexema(), SimbolosPosFixo.UNARIO);
			nextToken();
			analisaFator(variavelAtribuicao);
		} else if (Simbolos.isSAbreParenteses(this.token.getIdSimbolo())) {
			expressaoPosFixa.construirPosFixa(this.token.getLexema(), "parenteses");
			nextToken();
			if (Simbolos.isSNumero(this.token.getIdSimbolo())) {
				expressaoPosFixa.construirPosFixa(this.token.getLexema(), "numero");
				nextToken();
			}
			// fala pra passar o token como parametro
			analisaExpressao(variavelAtribuicao);

			if (Simbolos.isSFechaParenteses(this.token.getIdSimbolo())) {
				expressaoPosFixa.construirPosFixa(this.token.getLexema(), "parenteses");
				nextToken();
			} else {
				erroSintatico(this.token, "fechar parenteses");
			}
		} else
			if ((Simbolos.isSVerdadeiro(this.token.getIdSimbolo())) || (Simbolos.isSFalso(this.token.getIdSimbolo()))) {
			expressaoPosFixa.construirPosFixa(this.token.getLexema(), "bool");
			nextToken();
		}
	}

	private void analisaChamadaFuncao(Simbolo variavelAtribuicao) throws Exception {
		if (tabelaSimbolos.procurarDeclaracaoFuncaoTabela(this.token.getLexema())) {

			if (variavelAtribuicao != null) {
				Simbolo simbolo = tabelaSimbolos.oQueEuSou(this.token.getLexema());

				if (((Funcao) simbolo).getTipo()
						.equals(tabelaSimbolos.procuraTipoVariavel(variavelAtribuicao.getLexema()))
						|| ((Funcao) simbolo).getTipo()
								.equals(tabelaSimbolos.procuraTipoFuncao(variavelAtribuicao.getLexema()))) {
					geradorCodigo.escreveComando("CALL " + funcaoLabel.get(this.token.getLexema()));
					nextToken();
				} else {
					erroSemantico("A funcão '" + this.token.getLexema() + "' não é do tipo "
							+ tabelaSimbolos.procuraTipoVariavel(variavelAtribuicao.getLexema()) == null
									? tabelaSimbolos.procuraTipoFuncao(variavelAtribuicao.getLexema())
									: tabelaSimbolos.procuraTipoVariavel(variavelAtribuicao.getLexema()));
				}
			} else {
				geradorCodigo.escreveComando("CALL " + funcaoLabel.get(this.token.getLexema()));
				nextToken();
			}
		} else {
			erroSemantico("Função " + this.token.getLexema() + " fora de escopo ou não declarada");
		}

	}

	public void analisaEnquanto(boolean isFunc, ParametrosMN mn) throws Exception {
		int labelAux = labelCorrente;
		int labelAux2;
		nextToken();
		expressaoPosFixa.clear();
		geradorCodigo.escreveComando("L" + labelAux + ": NULL");
		labelCorrente++;
		analisaExpressao(null);
		expressaoPosFixa.setLista(expressaoPosFixa.getLista());

		if ("inteiro".equals(expressaoPosFixa.getTipoExpressao())) {
			erroSemantico("Comando 'enquanto' espera uma expressão booleana");
		}
		geradorCodigo.geraCodigoExpressao(expressaoPosFixa, tabelaSimbolos);
		
		if (Simbolos.isSFaca(this.token.getIdSimbolo()) || Simbolos.isSEnquanto(this.token.getIdSimbolo())) {
			labelAux2 = labelCorrente;
			geradorCodigo.escreveComando("JMPF L" + labelCorrente);
			labelCorrente++;
			nextToken();
			analisaComandoSimples(isFunc, mn);
			retornoFuncao = false;
			geradorCodigo.escreveComando("JMP L" + labelAux);
			geradorCodigo.escreveComando("L" + labelAux2 + ": NULL");
			labelCorrente++;
		} else {
			erroSintatico(this.token, "faca");
		}
	}

	public void analisaEscreva() throws Exception {
		nextToken();
		if (Simbolos.isSAbreParenteses(this.token.getIdSimbolo())) {
			nextToken();
			if (Simbolos.isSIdentificador(this.token.getIdSimbolo())) {
				/*
				 * se pesquisa_declvar_tabela(token.lexema) então inicio
				 * (pesquisa em toda a tabela)
				 */
				if (tabelaSimbolos.procurarInteiroTabela(this.token.getLexema())
						|| tabelaSimbolos.procurarDeclaracaoFuncaoTabela(this.token.getLexema())) {
					if (tabelaSimbolos.oQueEuSou(this.token.getLexema()) instanceof Variavel) {
						geradorCodigo.escreveComando("LDV " + tabelaSimbolos.oQueEuSou(this.token.getLexema()).getMemoria());
					} else {
						geradorCodigo.escreveComando("CALL " + funcaoLabel.get(this.token.getLexema()));
					}
					geradorCodigo.escreveComando("PRN");
					nextToken();
					if (Simbolos.isSFechaParenteses(this.token.getIdSimbolo())) {
						nextToken();
					} else {
						erroSintatico(this.token, "fecha parenteses");
					}
				}

				/* else tabela */
				else {
					erroTabelaDeSimbolos("Não foi encontrada a váriavel '" + this.token.getLexema()
							+ "' na tabela de simbolos (AnalisaEscreva) ou a mesma não é inteira");
				}

			} else {
				erroSintatico(this.token, "identificador");
			}
		} else {
			erroSintatico(this.token, "abre parenteses");
		}
	}

	public void analisaLeia() throws Exception {
		nextToken();
		if (Simbolos.isSAbreParenteses(this.token.getIdSimbolo())) {
			nextToken();
			if (Simbolos.isSIdentificador(this.token.getIdSimbolo())) {
				/*
				 * se pesquisa_declvar_tabela(token.lexema) então inicio
				 * (pesquisa em toda a tabela)
				 */
				// if(tabelaSimbolos.procurarDeclaracaoTabela(this.token.getLexema()))
				if (tabelaSimbolos.procurarInteiroTabela(this.token.getLexema())) // com
																					// semantico
				{
					geradorCodigo.escreveComando("RD");
					geradorCodigo.escreveComando("STR " + tabelaSimbolos.oQueEuSou(this.token.getLexema()).getMemoria());
					nextToken();
					if (Simbolos.isSFechaParenteses(this.token.getIdSimbolo())) {
						nextToken();
					} else {
						erroSintatico(this.token, "fecha parenteses");
					}
				}
				/* else tabela */
				else {
					erroTabelaDeSimbolos("Não foi encontrada a váriavel '" + this.token.getLexema()
							+ "' na tabela de simbolos (AnalisaLeia) ou a mesma não é inteira");
				}

			} else {
				erroSintatico(this.token, "identificador");
			}
		} else {
			erroSintatico(this.token, "abre parenteses");
		}

	}

	/**
	 * @category Classe auxiliar para gerar parametros m,n do comando alloc, dalloc e returnf
	 * @author Thales e Elton
	 *
	 */
	private class ParametrosMN {
		private int m, n;

		public ParametrosMN(int m, int n) {
			this.m = m;
			this.n = n;
		}

		public int getM() {
			return m;
		}

		public int getN() {
			return n;
		}

		public void incrementaN() {
			this.n++;
		}

	}
	

}
