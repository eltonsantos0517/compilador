package geracaodecodigo;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import posfixo.PosFixo;
import tabeladesimbolos.TabelaDeSimbolos;
import tabeladesimbolos.Variavel;

public class GeradorDeCodigo {

	private FileWriter arquivo;
	private PrintWriter gravaArquivo;
	public GeradorDeCodigo (String path) throws IOException
	{
		arquivo = new FileWriter(path);
		gravaArquivo = new PrintWriter(arquivo);
	}
	
	public void escreveComando(String comando)
	{
		gravaArquivo.println(comando.toUpperCase());
	}
	
	public void fechaArquivo () throws IOException
	{
		gravaArquivo.close();
		arquivo.close();
	}
	
	private String getCodigoOperador(String operador) {
		operador = operador.toUpperCase();
		switch (operador) {
		case "+":
			return "ADD";
		case "-":
			return "SUB";
		case "div":
			return "DIVI";
		case "*":
			return "MULT";
		case "OU":
			return "OR";
		case "E":
			return "AND";
		case "NAO":
			return "NEG";
		case ">":
			return "CMA";
		case "<":
			return "CME";
		case ">=":
			return "CMAQ";
		case "<=":
			return "CMEQ";
		case "=":
			return "CEQ";
		case "!=":
			return "CDIF";
		default:
			return null;
		}
	}

	private boolean isOperador(String operador) {
		operador = operador.toUpperCase();
		switch (operador) {
		case "+":
			return true;
		case "-":
			return true;
		case "div":
			return true;
		case "*":
			return true;
		case "OU":
			return true;
		case "E":
			return true;
		case "NAO":
			return true;
		case ">":
			return true;
		case "<":
			return true;
		case ">=":
			return true;
		case "<=":
			return true;
		case "=":
			return true;
		case "!=":
			return true;
		default:
			return false;
		}
	}

	public void geraCodigoExpressao(PosFixo expressaoPosFixa, TabelaDeSimbolos tabelaSimbolos) {
		List<String> expressao = expressaoPosFixa.getLista();
		List<String> tipos = expressaoPosFixa.getListaTipo();

		for (int i = 0; i < expressao.size(); i++) {

			if ( isOperador(expressao.get(i))) {
				if (tipos.get(i).equals("unario")) {
					if (expressao.get(i).equals("-")) {
						 escreveComando("INV");
					}
				} else {
					 escreveComando( getCodigoOperador(expressao.get(i)));
				}
			} else if (isNumero(expressao.get(i))) {
				 escreveComando("LDC " + expressao.get(i));
			} else if (expressao.get(i).equalsIgnoreCase("verdadeiro")) {
				 escreveComando("LDC 1");
			} else if (expressao.get(i).equalsIgnoreCase("falso")) {
				 escreveComando("LDC 0");
			} else {
				if (tabelaSimbolos.oQueEuSou(expressao.get(i)) instanceof Variavel) {
					 escreveComando("LDV " + (tabelaSimbolos.oQueEuSou(expressao.get(i))).getMemoria());
				}
			}
		}
	}
	
	private boolean isNumero(String comando) {
		return comando.startsWith("0") || comando.startsWith("1") || comando.startsWith("2") || comando.startsWith("3")
				|| comando.startsWith("4") || comando.startsWith("5") || comando.startsWith("6")
				|| comando.startsWith("7") || comando.startsWith("8") || comando.startsWith("9");
	}


}
