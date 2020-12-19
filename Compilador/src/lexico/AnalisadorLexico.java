package lexico;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AnalisadorLexico {
	private Token valores = new Token();
	private int valor;
	private int colPrimeiroCarac;
	private int linha;
	private InputStreamReader leitor;
	private List<Token> listaTokens;
	
	public AnalisadorLexico(String arquivo) throws IOException {
		this.linha=1;
		this.colPrimeiroCarac=0;
		this.valor=0;
		
		this.listaTokens = new ArrayList<Token>();
		this.leitor = new InputStreamReader(new FileInputStream(arquivo));

		this.valor = leitor.read();

		// while para ler o arquivo todo
		while (valor != -1) {
			ignora(leitor);
			// abriu chaves
			if (valor == 123) {
				// enquanto nï¿½o for final do
				// arquivo ou fecha chaves
				while (valor != 125 && valor != -1) {
					if (valor == 10) {
						linha++;
					}
					valor = leitor.read();
				}
				//TODO
				if (valor == -1) { 
					System.out.println("WARNING - Comentario nao foi fechado!");
					/*WARNING Nï¿½o fechou comentario
					throw new Exception("O caracter } nï¿½o foi encontrado.");*/
				}

				valor = leitor.read();
				ignora(leitor);
			}
			char letra = (char) this.valor;
			this.colPrimeiroCarac++;
			if(this.valor == 9 || this.valor == 13 || this.valor == 10 || this.valor == 32)
			{
				ignora(leitor);

			}
			else if(this.valor != -1)
			{
				listaTokens.add(new Token(pegaToken(letra)));
			}

		}
	}

	// funï¿½ï¿½o que ignora espaï¿½os, enter, tab e carriage return
	public void ignora(InputStreamReader leitor) throws IOException {
		while (this.valor == 9) { //tab
			this.valor = leitor.read();
		}
		if (this.valor == 13) { //CR
			this.valor = leitor.read();
		}

		if (this.valor == 10) { //enter
			this.linha++;
			this.colPrimeiroCarac = 0;
			this.valor = leitor.read();
			if (this.valor == 9)
			{
				while (this.valor == 9) {
					this.valor = leitor.read();
				}
			}
		}

		if (this.valor == 32) // para ignorar espaï¿½os
		{
			this.colPrimeiroCarac = this.colPrimeiroCarac + 1;
			this.valor = leitor.read();

		}

	}

	public Token pegaToken(char caracter) throws IOException{
		
		if (Character.isLetter(caracter) == true) {
			trataLetra(caracter);
		} else if (isNumber(caracter) == true) {
			trataDigito(caracter);
		} else if (caracter == ':') {
			trataAtribuicao(caracter);
		} else if (caracter == '+' || caracter == '-' || caracter == '*') {
			trataOperacaoAritmetica(caracter);
			this.valor = leitor.read();
		} else if (caracter == '<' || caracter == '>' || caracter == '=' || caracter == '!') {
			trataOperadorRelacional(caracter);
		} else if (caracter == ';' || caracter == ',' || caracter == '(' || caracter == ')' || caracter == '.') {
			trataPontuacao(caracter);
			this.valor = leitor.read();
		} else {
			
			// nï¿½o caiu em nenhum caso
			if (caracter == '}') {
				valores.setSimbolo("serro");
				valores.setIdSimbolo(40);
				valores.setLinha(linha);
				valores.setColunaPrimeiroCaracter(colPrimeiroCarac);
				valores.setColunaUltimoCaracter(colPrimeiroCarac);
				valores.setLexema(caracter+"");
				valores.setdescricaoErro("linha:" + linha + " - coluna:"
						+ this.colPrimeiroCarac + " - Nao foi encontrado o caracter { antes do }");
				
				/*throw new Exception("Caracter Invï¿½lido (" + caracter + ") - linha:" + linha + " - coluna:"
						+ this.colPrimeiroCarac + " - Nï¿½o foi encontrado o caracter { antes do }");*/
				this.valor = leitor.read();
			} else{ 
				valores.setSimbolo("serro");
				valores.setIdSimbolo(40);
				valores.setLinha(linha);
				valores.setColunaPrimeiroCaracter(colPrimeiroCarac);
				valores.setColunaUltimoCaracter(colPrimeiroCarac);
				valores.setLexema(caracter+"");
				valores.setdescricaoErro("linha:" + linha + " - coluna:" + this.colPrimeiroCarac);
				this.valor = leitor.read();
				
				/*throw new Exception(
						"Caracter Invï¿½lido (" + caracter + ") - linha:" + linha + " - coluna:" + this.colPrimeiroCarac);*/
			}
		}
		this.colPrimeiroCarac = valores.getColunaUltimoCaracter();
		return valores;

	}

	void trataDigito(char caracter) throws IOException {
		String palavra = "";
		int ultimoCaracter = this.colPrimeiroCarac - 1;
		while (isNumber(caracter) == true) {
			palavra = palavra + caracter;
			ultimoCaracter++;
			this.valor = leitor.read();
			caracter = (char) this.valor;
		}
		valores.setColunaPrimeiroCaracter(this.colPrimeiroCarac);
		valores.setColunaUltimoCaracter(ultimoCaracter);
		valores.setLinha(this.linha);
		valores.setLexema(palavra);
		valores.setSimbolo("snumero");
		valores.setIdSimbolo(23);
	}

	void trataLetra(char caracter) throws IOException {
		String palavra = "";
		int ultimo_caracter = this.colPrimeiroCarac - 1;
		while (Character.isLetter(caracter) == true || isNumber(caracter) == true || caracter == '_') {
			palavra = palavra + caracter;
			ultimo_caracter++;
			this.valor = this.leitor.read();
			caracter = (char) this.valor;
		}
		selector(palavra, ultimo_caracter);
	}

	void trataAtribuicao(char caracter) throws IOException {
		String palavra = "" + caracter;
		this.valor = leitor.read();
		caracter = (char) this.valor;
		if (caracter == '=') {
			palavra = palavra + caracter;
			valores.setLexema(palavra);
			valores.setLinha(linha);
			valores.setColunaPrimeiroCaracter(this.colPrimeiroCarac);
			valores.setColunaUltimoCaracter((this.colPrimeiroCarac + 1));
			valores.setSimbolo("satribuicao");
			valores.setdescricaoErro("-");
			valores.setIdSimbolo(24);
			this.valor = leitor.read();
		} else {
			valores.setLexema(palavra);
			valores.setLinha(linha);
			valores.setColunaPrimeiroCaracter(this.colPrimeiroCarac);
			valores.setColunaUltimoCaracter(this.colPrimeiroCarac);
			valores.setSimbolo("sdoispontos");
			valores.setdescricaoErro("-");
			valores.setIdSimbolo(39);
		}

	}

	void trataOperacaoAritmetica(char caracter) {
		String palavra = "" + caracter;
		valores.setLexema(palavra);
		valores.setLinha(linha);
		valores.setColunaPrimeiroCaracter(this.colPrimeiroCarac);
		valores.setColunaUltimoCaracter(this.colPrimeiroCarac);
		valores.setdescricaoErro("-");
		if (caracter == '+') {
			valores.setSimbolo("smais");
			valores.setIdSimbolo(36);
		} else if (caracter == '-') {
			valores.setSimbolo("smenos");
			valores.setIdSimbolo(37);
		} else if (caracter == '*') {
			valores.setSimbolo("smultiplicacao");
			valores.setIdSimbolo(38);
		}
	}

	void trataOperadorRelacional(char caracter) throws IOException {
		String palavra = "" + caracter;
		int colUltimoCarac = this.colPrimeiroCarac - 1;
		valores.setLinha(linha);
		valores.setColunaPrimeiroCaracter(this.colPrimeiroCarac);
		valores.setdescricaoErro("-");
		if (caracter == '>') {
			this.valor = leitor.read();
			caracter = (char) this.valor;
			if (caracter == '=') {
				palavra = palavra + caracter;
				colUltimoCarac++;
				valores.setColunaUltimoCaracter(colUltimoCarac);
				valores.setLexema(palavra);
				valores.setSimbolo("smaiorigual");
				valores.setIdSimbolo(31);
				this.valor = leitor.read();

			} else {
				valores.setSimbolo("smaior");
				valores.setIdSimbolo(30);
				valores.setColunaUltimoCaracter(this.colPrimeiroCarac);
				valores.setLexema(palavra);
			}

		} else if (caracter == '<') {
			this.valor = leitor.read();
			caracter = (char) this.valor;
			if (caracter == '=') {
				palavra = palavra + caracter;
				colUltimoCarac++;
				valores.setColunaUltimoCaracter(colUltimoCarac);
				valores.setLexema(palavra);
				valores.setSimbolo("smenorigual");
				valores.setIdSimbolo(34);
				this.valor = leitor.read();
			} else {
				valores.setSimbolo("smenor");
				valores.setIdSimbolo(33);
				valores.setColunaUltimoCaracter(this.colPrimeiroCarac);
				valores.setLexema(palavra);
			}
		} else if (caracter == '!') {
			this.valor = leitor.read();
			caracter = (char) this.valor;
			if (caracter == '=') {
				palavra = palavra + caracter;
				colUltimoCarac = colUltimoCarac + 2;
				valores.setColunaUltimoCaracter(colUltimoCarac);
				valores.setLexema(palavra);
				valores.setSimbolo("sdiferente");
				valores.setIdSimbolo(35);
				this.valor = leitor.read();
			} else {
				valores.setSimbolo("serro");
				valores.setIdSimbolo(40);
				valores.setLinha(linha);
				valores.setColunaPrimeiroCaracter(colPrimeiroCarac);
				valores.setColunaUltimoCaracter(colPrimeiroCarac);
				valores.setLexema(caracter+"");
				valores.setdescricaoErro("! sem = após (DIFERENTE)- linha:" + linha + " - coluna:" + this.colPrimeiroCarac);
				this.valor = leitor.read();

				/*throw new Exception("! sem = apï¿½s - linha:" + linha + " - coluna:" + this.colPrimeiroCarac);*/

			}
		} else if (caracter == '=') {
			colUltimoCarac++;
			valores.setColunaUltimoCaracter(colUltimoCarac);
			valores.setLexema(palavra);
			valores.setSimbolo("sigual");
			valores.setIdSimbolo(32);
			this.valor = leitor.read();
		}
	}

	void trataPontuacao(char caracter) {
		String palavra = "" + caracter;
		valores.setColunaPrimeiroCaracter(this.colPrimeiroCarac);
		valores.setColunaUltimoCaracter(this.colPrimeiroCarac);
		valores.setLexema(palavra);
		valores.setdescricaoErro("-");
		valores.setLinha(linha);
		if (caracter == '.') {
			valores.setSimbolo("sponto");
			valores.setIdSimbolo(25);
		} else if (caracter == ';') {
			valores.setSimbolo("sponto_virgula");
			valores.setIdSimbolo(26);
		} else if (caracter == ',') {
			valores.setSimbolo("svirgula");
			valores.setIdSimbolo(27);
		} else if (caracter == '(') {
			valores.setSimbolo("sabre_parenteses");
			valores.setIdSimbolo(28);
		} else if (caracter == ')') {
			valores.setSimbolo("sfecha_parenteses");
			valores.setIdSimbolo(29);
		}

	}

	public boolean isNumber(char letra) {
		String letras = "" + letra;
		try {
			Integer.parseInt(letras);
		} catch (Exception e) {
			return false;

		}
		return true;

	}

	private void selector(String palavra, Integer colUltimoCarac) {
		if (palavra.equalsIgnoreCase("programa") == true) {
			valores.setLexema(palavra.toLowerCase());
			valores.setSimbolo("s" + palavra.toLowerCase());
			valores.setIdSimbolo(1);
			valores.setColunaPrimeiroCaracter(this.colPrimeiroCarac);
			valores.setColunaUltimoCaracter(colUltimoCarac);
			valores.setdescricaoErro("-");
			valores.setLinha(linha);
		} else if (palavra.equalsIgnoreCase("se") == true) {
			valores.setLexema(palavra.toLowerCase());
			valores.setSimbolo("s" + palavra.toLowerCase());
			valores.setIdSimbolo(2);
			valores.setColunaPrimeiroCaracter(this.colPrimeiroCarac);
			valores.setColunaUltimoCaracter(colUltimoCarac);
			valores.setdescricaoErro("-");
			valores.setLinha(linha);
		} else if (palavra.equalsIgnoreCase("entao") == true) {
			valores.setLexema(palavra.toLowerCase());
			valores.setSimbolo("s" + palavra.toLowerCase());
			valores.setIdSimbolo(3);
			valores.setColunaPrimeiroCaracter(this.colPrimeiroCarac);
			valores.setColunaUltimoCaracter(colUltimoCarac);
			valores.setdescricaoErro("-");
			valores.setLinha(linha);
		} else if (palavra.equalsIgnoreCase("senao") == true) {
			valores.setLexema(palavra.toLowerCase());
			valores.setSimbolo("s" + palavra.toLowerCase());
			valores.setIdSimbolo(4);
			valores.setColunaPrimeiroCaracter(this.colPrimeiroCarac);
			valores.setColunaUltimoCaracter(colUltimoCarac);
			valores.setdescricaoErro("-");
			valores.setLinha(linha);
		} else if (palavra.equalsIgnoreCase("enquanto") == true) {
			valores.setLexema(palavra.toLowerCase());
			valores.setSimbolo("s" + palavra.toLowerCase());
			valores.setIdSimbolo(5);
			valores.setColunaPrimeiroCaracter(this.colPrimeiroCarac);
			valores.setColunaUltimoCaracter(colUltimoCarac);
			valores.setdescricaoErro("-");
			valores.setLinha(linha);
		} else if (palavra.equalsIgnoreCase("faca") == true) {
			valores.setLexema(palavra.toLowerCase());
			valores.setSimbolo("s" + palavra.toLowerCase());
			valores.setIdSimbolo(6);
			valores.setColunaPrimeiroCaracter(this.colPrimeiroCarac);
			valores.setColunaUltimoCaracter(colUltimoCarac);
			valores.setdescricaoErro("-");
			valores.setLinha(linha);
		} else if (palavra.equalsIgnoreCase("inicio") == true) {
			valores.setLexema(palavra.toLowerCase());
			valores.setSimbolo("s" + palavra.toLowerCase());
			valores.setIdSimbolo(7);
			valores.setColunaPrimeiroCaracter(this.colPrimeiroCarac);
			valores.setColunaUltimoCaracter(colUltimoCarac);
			valores.setdescricaoErro("-");
			valores.setLinha(linha);
		} else if (palavra.equalsIgnoreCase("fim") == true) {
			valores.setLexema(palavra.toLowerCase());
			valores.setSimbolo("s" + palavra.toLowerCase());
			valores.setIdSimbolo(8);
			valores.setColunaPrimeiroCaracter(this.colPrimeiroCarac);
			valores.setColunaUltimoCaracter(colUltimoCarac);
			valores.setdescricaoErro("-");
			valores.setLinha(linha);
		} else if (palavra.equalsIgnoreCase("escreva") == true) {
			valores.setLexema(palavra.toLowerCase());
			valores.setSimbolo("s" + palavra.toLowerCase());
			valores.setIdSimbolo(9);
			valores.setColunaPrimeiroCaracter(this.colPrimeiroCarac);
			valores.setColunaUltimoCaracter(colUltimoCarac);
			valores.setdescricaoErro("-");
			valores.setLinha(linha);
			this.colPrimeiroCarac = colUltimoCarac;
		} else if (palavra.equalsIgnoreCase("leia") == true) {
			valores.setLexema(palavra.toLowerCase());
			valores.setSimbolo("s" + palavra.toLowerCase());
			valores.setIdSimbolo(10);
			valores.setColunaPrimeiroCaracter(this.colPrimeiroCarac);
			valores.setColunaUltimoCaracter(colUltimoCarac);
			valores.setdescricaoErro("-");
			valores.setLinha(linha);
		} else if (palavra.equalsIgnoreCase("var") == true) {
			valores.setLexema(palavra.toLowerCase());
			valores.setSimbolo("s" + palavra.toLowerCase());
			valores.setIdSimbolo(11);
			valores.setColunaPrimeiroCaracter(this.colPrimeiroCarac);
			valores.setColunaUltimoCaracter(colUltimoCarac);
			valores.setdescricaoErro("-");
			valores.setLinha(linha);
		} else if (palavra.equalsIgnoreCase("inteiro") == true) {
			valores.setLexema(palavra.toLowerCase());
			valores.setSimbolo("s" + palavra.toLowerCase());
			valores.setIdSimbolo(12);
			valores.setColunaPrimeiroCaracter(this.colPrimeiroCarac);
			valores.setColunaUltimoCaracter(colUltimoCarac);
			valores.setdescricaoErro("-");
			valores.setLinha(linha);
		} else if (palavra.equalsIgnoreCase("booleano") == true) {
			valores.setLexema(palavra.toLowerCase());
			valores.setSimbolo("s" + palavra.toLowerCase());
			valores.setIdSimbolo(13);
			valores.setColunaPrimeiroCaracter(this.colPrimeiroCarac);
			valores.setColunaUltimoCaracter(colUltimoCarac);
			valores.setdescricaoErro("-");
			valores.setLinha(linha);
		} else if (palavra.equalsIgnoreCase("verdadeiro") == true) {
			valores.setLexema(palavra.toLowerCase());
			valores.setSimbolo("s" + palavra.toLowerCase());
			valores.setIdSimbolo(14);
			valores.setColunaPrimeiroCaracter(this.colPrimeiroCarac);
			valores.setColunaUltimoCaracter(colUltimoCarac);
			valores.setdescricaoErro("-");
			valores.setLinha(linha);
		} else if (palavra.equalsIgnoreCase("falso") == true) {
			valores.setLexema(palavra.toLowerCase());
			valores.setSimbolo("s" + palavra.toLowerCase());
			valores.setIdSimbolo(15);
			valores.setColunaPrimeiroCaracter(this.colPrimeiroCarac);
			valores.setColunaUltimoCaracter(colUltimoCarac);
			valores.setdescricaoErro("-");
			valores.setLinha(linha);
		} else if (palavra.equalsIgnoreCase("procedimento") == true) {
			valores.setLexema(palavra.toLowerCase());
			valores.setSimbolo("s" + palavra.toLowerCase());
			valores.setIdSimbolo(16);
			valores.setColunaPrimeiroCaracter(this.colPrimeiroCarac);
			valores.setColunaUltimoCaracter(colUltimoCarac);
			valores.setdescricaoErro("-");
			valores.setLinha(linha);
		} else if (palavra.equalsIgnoreCase("funcao") == true) {
			valores.setLexema(palavra.toLowerCase());
			valores.setSimbolo("s" + palavra.toLowerCase());
			valores.setIdSimbolo(17);
			valores.setColunaPrimeiroCaracter(this.colPrimeiroCarac);
			valores.setColunaUltimoCaracter(colUltimoCarac);
			valores.setdescricaoErro("-");
			valores.setLinha(linha);
		} else if (palavra.equalsIgnoreCase("div") == true) {
			valores.setLexema(palavra.toLowerCase());
			valores.setSimbolo("s" + palavra.toLowerCase());
			valores.setIdSimbolo(18);
			valores.setColunaPrimeiroCaracter(this.colPrimeiroCarac);
			valores.setColunaUltimoCaracter(colUltimoCarac);
			valores.setdescricaoErro("-");
			valores.setLinha(linha);
		} else if (palavra.equalsIgnoreCase("e") == true) {
			valores.setLexema(palavra.toLowerCase());
			valores.setSimbolo("s" + palavra.toLowerCase());
			valores.setIdSimbolo(19);
			valores.setColunaPrimeiroCaracter(this.colPrimeiroCarac);
			valores.setColunaUltimoCaracter(colUltimoCarac);
			valores.setdescricaoErro("-");
			valores.setLinha(linha);
		} else if (palavra.equalsIgnoreCase("ou") == true) {
			valores.setLexema(palavra.toLowerCase());
			valores.setSimbolo("s" + palavra.toLowerCase());
			valores.setIdSimbolo(20);
			valores.setColunaPrimeiroCaracter(this.colPrimeiroCarac);
			valores.setColunaUltimoCaracter(colUltimoCarac);
			valores.setdescricaoErro("-");
			valores.setLinha(linha);
		} else if (palavra.equalsIgnoreCase("nao") == true) {
			valores.setLexema(palavra.toLowerCase());
			valores.setSimbolo("s" + palavra.toLowerCase());
			valores.setIdSimbolo(21);
			valores.setColunaPrimeiroCaracter(this.colPrimeiroCarac);
			valores.setColunaUltimoCaracter(colUltimoCarac);
			valores.setdescricaoErro("-");
			valores.setLinha(linha);
		} else // ï¿½ um identificador
		{
			valores.setLexema(palavra.toLowerCase());
			valores.setSimbolo("sidentificador");
			valores.setIdSimbolo(22);
			valores.setColunaPrimeiroCaracter(this.colPrimeiroCarac);
			valores.setColunaUltimoCaracter(colUltimoCarac);
			valores.setdescricaoErro("-");
			valores.setLinha(linha);
		}
	}

	public List<Token> getListaTokens() {
		return listaTokens;
	}
	
	private int position = -1;
	public Token getToken(){
		position++;
		try{
			return listaTokens.get(position);
		}catch(Exception e){
			return null;
		}
		
	}
}
