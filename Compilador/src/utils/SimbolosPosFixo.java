package utils;

public class SimbolosPosFixo {
	/*
    private static int unario = 6;
    private static int multDiv = 5;
    private static int somaSub = 4;
    private static int comparacao = 3;
    private static int e = 2;
    private static int ou = 1;
    private static int parenteses = 0;
    */
	
	public static String UNARIO = "unario";
	
	public static int valor(String simbolo)
	{
		if (simbolo.equals(UNARIO) || simbolo.equals("Unao") || simbolo.equals("U+") || simbolo.equals("U-"))//+,-,nao
		{
			return 6;
		}
		else if (simbolo.equals("*") || simbolo.equals("div"))
		{
			return 5;
		}
		else if (simbolo.equals("+")|| simbolo.equals("-"))
		{
			return 4;
		}
		else if(simbolo.equals(">")||simbolo.equals(">=")||simbolo.equals("<")||simbolo.equals("<=")||simbolo.equals("!=")||simbolo.equals("="))
		{
			return 3;
		}
		else if (simbolo.equals("e"))
		{
			return 2;
		}
		else if (simbolo.equals("ou"))
		{
			return 1;
		}
		else if (simbolo.equals(")"))
		{
			return -1;
		}
		else // simbolo (
			return -1;
	}
}
