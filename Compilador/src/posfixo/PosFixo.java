package posfixo;

import java.util.ArrayList;
import java.util.List;

import utils.SimbolosPosFixo;

/*
(Unarias) +,-,nao
*,div
+,-
<,>,<=,>=,=,!=
e
ou
*/
public class PosFixo {

	private List<String> tabelaOperandos = new ArrayList<>();
	private List<String> lista = new ArrayList<>();
	private List<String> listaTipo = new ArrayList<>();

	int tamanho;
	String tipoExpressao = "nada";

	private List<String> listaTipoAux;
	private List<String> listaAux;

	public void analise() throws Exception{

    	int i = 0;
    	
    	if(listaTipoAux.size() == 1){
    		tipoExpressao = listaTipoAux.get(0);
    		return;
    	}else if (listaTipoAux.size() == 2){
    		if(listaTipoAux.get(1).equals("unario"))
    		{
    			if(listaTipoAux.get(0).equals("inteiro") && (listaAux.get(1).equals("+") || listaAux.get(1).equals("-")))
    			{
    				tipoExpressao = "inteiro";
    				return;
    			}
    			else if (listaTipoAux.get(0).equals("booleano") && (listaAux.get(1).equals("nao")))
    			{
    				tipoExpressao = "booleano";
    				return;
    			}
    			else
    			{
    				throw new Exception("Há algo de errado com sua expressão");
    			}
    		}
    		else{
    		throw new Exception("Há algo de errado com sua expressão");
    		}
    	}
    	try{
	    	while( !( listaTipoAux.get(i).equals("operacao") || listaTipoAux.get(i).equals("relacional")  || listaTipoAux.get(i).equals("unario") || listaTipoAux.get(i).equals("logico")) ){
	    		i++;
	    	}
    	}catch (IndexOutOfBoundsException e){
    		//acabou a lista
    		return;
    	}
    	
    	
    	if(listaTipoAux.get(i).equals("operacao") && listaTipoAux.get(i-1).equals("inteiro") && listaTipoAux.get(i-2).equals("inteiro")){
    		listaTipoAux.remove(i);
    		listaTipoAux.remove(i-1);
    		listaAux.remove(i);
    		listaAux.remove(i-1);
    		tipoExpressao = "inteiro";
    	}else if(listaTipoAux.get(i).equals("relacional") && listaTipoAux.get(i-1).equals("inteiro") && listaTipoAux.get(i-2).equals("inteiro")){
    		listaTipoAux.remove(i);
    		listaTipoAux.remove(i-1);
    		listaAux.remove(i);
    		listaAux.remove(i-1);
    		listaTipoAux.set(i-2, "booleano");
    		tipoExpressao = "booleano";
    	}else if (listaTipoAux.get(i).equals("unario") && listaTipoAux.get(i-1).equals("booleano") && listaAux.get(i).equals("nao")){
        		listaTipoAux.remove(i);
        		listaAux.remove(i);
        		tipoExpressao = "booleano";
        }else if (listaTipoAux.get(i).equals("unario") && listaTipoAux.get(i-1).equals("inteiro")){
        		listaTipoAux.remove(i);
        		listaAux.remove(i);
        		tipoExpressao = "inteiro";
        }else if (listaTipoAux.get(i).equals("logico") && listaTipoAux.get(i-1).equals("booleano") && listaTipoAux.get(i-2).equals("booleano")){
        		listaTipoAux.remove(i);
        		listaTipoAux.remove(i-1);
        		listaAux.remove(i);
        		listaAux.remove(i-1);
        		tipoExpressao = "booleano";
        	
    	}else if(listaTipoAux.get(i).equals("relacional") && listaTipoAux.get(i-1).equals("booleano") && listaTipoAux.get(i-2).equals("booleano")){
    		if(listaAux.get(i).equals("=") || listaAux.get(i).equals("!=")){
        		listaTipoAux.remove(i);
        		listaTipoAux.remove(i-1);
        		listaAux.remove(i);
        		listaAux.remove(i-1);
        		tipoExpressao = "booleano";
    		}else{
    			throw new Exception("Erro semantico - Comparacao de booleanos com > ou <");
    		}
    	}else {
    		throw new Exception("Erro semantico -> Inconsistêcia entre tipo e operador");//" + listaAux.get(i-2) +"' "+ listaAux.get(i) + " '" + listaAux.get(i-1)+"'");
    	}
    	
    	analise();
    }

	public void insereTabelaOperandos(String simbolo) {
		tabelaOperandos.add(simbolo);
	}

	public void insereLista(String simbolo) {
		lista.add(simbolo);
	}

	public void insereTipo(String tipo, int valor) {
		if (valor == 6) {
			listaTipo.add("unario");
		} else if (valor == 5 || valor == 4) {
			listaTipo.add("operacao");
		} else if (valor == 3) {
			listaTipo.add("relacional");
		} else if (valor == 2 || valor == 1) {
			listaTipo.add("logico");
		} else {
			listaTipo.add(tipo);
		}
	}

	public void construirPosFixa(String simbolo, String tipo) {
		if (tipo.equals("inteiro") || tipo.equals("numero") || tipo.equals("bool") || tipo.equals("booleano")) {
			if (tipo.equals("numero")) {
				insereTipo("inteiro", 0);
			} else if (tipo.equals("bool")) {
				insereTipo("booleano", 0);
			} else {
				insereTipo(tipo, 0);
			}

			insereLista(simbolo);

		} else {
			if (tabelaOperandos.size() == 0 || simbolo.equals("(")) {
				if(tipo.equals(SimbolosPosFixo.UNARIO))
				{
					insereTabelaOperandos("U"+simbolo);
				}
				else
				{
					insereTabelaOperandos(simbolo);
				}
			} else if (SimbolosPosFixo.valor(simbolo) == -1) // simbolo )
			{
				while (!tabelaOperandos.get(tabelaOperandos.size() - 1).equals("(")) {
					
					int valor = SimbolosPosFixo.valor(tabelaOperandos.get(tabelaOperandos.size() - 1));
					if(valor==6)
					{
						insereLista(tabelaOperandos.get(tabelaOperandos.size() - 1).substring(1));
					}
					else
					{
						insereLista(tabelaOperandos.get(tabelaOperandos.size() - 1));
					}
					insereTipo("tabelaOperandos", valor);
					tabelaOperandos.remove(tabelaOperandos.size() - 1);

				}
				tabelaOperandos.remove(tabelaOperandos.size() - 1);
			} else {
				while (SimbolosPosFixo.valor(tabelaOperandos.get(tabelaOperandos.size() - 1)) >= SimbolosPosFixo
						.valor(simbolo)) // desempilhar e colocar na lista
				{
					int valor = SimbolosPosFixo.valor(tabelaOperandos.get(tabelaOperandos.size() - 1));
					if(valor==6)
					{
						insereLista(tabelaOperandos.get(tabelaOperandos.size() - 1).substring(1));
					}
					else
					{
						insereLista(tabelaOperandos.get(tabelaOperandos.size() - 1));
					}
					insereTipo("tabelaOperandos", valor);
					tabelaOperandos.remove(tabelaOperandos.size() - 1);
					if (tabelaOperandos.size() == 0) {
						break;
					}
				}
				if(tipo.equals(SimbolosPosFixo.UNARIO))
				{
					insereTabelaOperandos("U"+simbolo);
				}
				else
				{
				insereTabelaOperandos(simbolo);
				}
			}
		}

	}

	public void limpaOperandosInsereLista() {
		while (tabelaOperandos.size() > 0)// limpa o que restou da tabela de
											// operandos
		{
			int valor = SimbolosPosFixo.valor(tabelaOperandos.get(tabelaOperandos.size() - 1));
			if(valor==6)
			{
				insereLista(tabelaOperandos.get(tabelaOperandos.size() - 1).substring(1));
			}
			else
			{
				insereLista(tabelaOperandos.get(tabelaOperandos.size() - 1));
			}
			insereTipo("tabelaOperandos", valor);
			tabelaOperandos.remove(tabelaOperandos.size() - 1);
		}
	}

	public List<String> getLista() {
		limpaOperandosInsereLista();
		return lista;
	}

	public List<String> getListaTipo() {
		return listaTipo;
	}

	public void setLista(List<String> lista) {
		this.lista = lista;
	}

	public String getTipoExpressao() throws Exception {
		listaTipoAux = new ArrayList<>(listaTipo);
		listaAux = new ArrayList<>(lista);
		analise();
		return tipoExpressao;
	}

	public void clear() {
		lista.clear();
		listaTipo.clear();
	}

}
