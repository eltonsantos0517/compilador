package tabeladesimbolos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import tabeladesimbolos.Funcao;
import tabeladesimbolos.Variavel;

public class TabelaDeSimbolos {
	private List<Simbolo> simbolos;
	private Collection<Simbolo> lixeira = new ArrayList<>();

	public TabelaDeSimbolos() {
		simbolos = new ArrayList<>();
	}

	public void inserirTabela(Simbolo simbolo) {
		simbolos.add(simbolo);
	}

	public void inserirTipoFuncao(String tipo) {
		((Funcao) simbolos.get(simbolos.size() - 1)).setTipo(tipo);
	}

	public void inserirTipoVariavel(String tipo) {

		int size = simbolos.size() - 1;
		for (int i = size; i >= 0; i--) {
			if (simbolos.get(i) instanceof Variavel) {
				if (((Variavel) simbolos.get(i)).getTipo() == null) {
					((Variavel) simbolos.get(i)).setTipo(tipo);
				}
			}
		}
	}

	public String procuraTipoVariavel(String lexema) {
		int i;
		for (i = simbolos.size() - 1; i >= 0; i--) {
			if (simbolos.get(i).getLexema().equals(lexema)) {
				if (simbolos.get(i) instanceof Variavel)
					return ((Variavel) simbolos.get(i)).getTipo();
			}
		}

		return null;
	}
	
	public String procuraTipoFuncao(String lexema) {
		int i;
		for (i = simbolos.size() - 1; i >= 0; i--) {
			if (simbolos.get(i).getLexema().equals(lexema)) {
				if (simbolos.get(i) instanceof Funcao)
					return ((Funcao) simbolos.get(i)).getTipo();
			}
		}

		return null;
	}

	public boolean procuraDuplicidadeNomeVariavel(String lexema) {

		return procurarDeclaracaoFuncaoTabela(lexema) || procurarDeclaracaoProcedimentoTabela(lexema)
				|| procurarDuplicacaoVariavelTabela(lexema);

	}

	public boolean procurarDuplicacaoVariavelTabela(String lexema) {
		int i;
		for (i = simbolos.size() - 1; i >= 0; i--) {
			if (simbolos.get(i) instanceof Procedimento || simbolos.get(i) instanceof Funcao) {
				break;
			}
			if (simbolos.get(i).getLexema().equals(lexema)) {

				return true;
			}
		}

		return false;

	}

	public boolean procurarDeclaracaoTabela(String lexema) {
		int i;
		for (i = simbolos.size() - 1; i >= 0; i--) {
			if (simbolos.get(i).getLexema().equals(lexema)) {
				return true;
			}
		}

		return false;
	}

	public boolean procurarDeclaracaoProcedimentoTabela(String lexema) {
		int i;
		for (i = simbolos.size() - 1; i >= 0; i--) {
			if (simbolos.get(i).getLexema().equals(lexema)) {
				if (simbolos.get(i) instanceof Procedimento) {
					return true;
				}

			}
		}

		return false;
	}

	public boolean procurarDeclaracaoFuncaoTabela(String lexema) {
		int i;
		for (i = simbolos.size() - 1; i >= 0; i--) {
			if (simbolos.get(i).getLexema().equals(lexema)) {
				if (simbolos.get(i) instanceof Funcao) {
					return true;
				}

			}
		}

		return false;
	}

	public boolean procurarBooleanoTabela(String lexema) {
		int i;
		for (i = simbolos.size() - 1; i >= 0; i--) {
			if (simbolos.get(i).getLexema().equals(lexema)) {
				if (simbolos.get(i) instanceof Variavel) {
					if (((Variavel) simbolos.get(i)).getTipo() == "inteiro") // (Variavel)simbolos.get(i)).getTipo().equals("inteiro"))
					{
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}

			}
		}

		return false;

	}

	public boolean procurarInteiroTabela(String lexema) {
		int i;
		for (i = simbolos.size() - 1; i >= 0; i--) {
			if (simbolos.get(i).getLexema().equals(lexema)) {
				if (simbolos.get(i) instanceof Variavel) {
					if (((Variavel) simbolos.get(i)).getTipo().equals("inteiro")) // (Variavel)simbolos.get(i)).getTipo().equals("inteiro"))
					{
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}

			}
		}

		return false;

	}

	public Simbolo oQueEuSou(String lexema) {
		int i;
		for (i = simbolos.size() - 1; i >= 0; i--) {
			if (simbolos.get(i).getLexema().equals(lexema)) {
				return simbolos.get(i);
			}
		}
		return null;
	}

	public void retiraEscopo() {
		int i;
		for (i = simbolos.size() - 1; i >= 0; i--) {
			if (simbolos.get(i) instanceof Procedimento || simbolos.get(i) instanceof Funcao) {
				break;
			}
			lixeira.add(simbolos.get(i));
		}
		simbolos.removeAll(lixeira);
	}

	public List<Simbolo> getSimbolos() {
		return simbolos;
	}

	public void setSimbolos(List<Simbolo> simbolos) {
		this.simbolos = simbolos;
	}

}
