package utils;

public class Simbolos {
	private static int S_PROGRAMA = 1;
	private static int S_IDENTIFICADOR = 22;
	private static int S_PONTO_E_VIRGULA = 26;
	private static int S_PONTO = 25;
	private static int S_VAR = 11;
	private static int S_DOIS_PONTOS = 39;
	private static int S_VIRGULA = 27;
	private static int S_INTEIRO = 12;
	private static int S_BOOLEANO = 13;
	private static int S_PROCEDIMENTO = 16;
	private static int S_FUNCAO = 17;
	private static int S_INICIO = 7;
	private static int S_FIM = 8;
	private static int S_SE = 2;
	private static int S_ENQUANTO = 5;
	private static int S_FACA = 6;
	private static int S_LEIA = 10;
	private static int S_ESCREVA = 9;
	private static int S_ENTAO = 3;
	private static int S_SENAO = 4;
	private static int S_MAIOR = 30;
	private static int S_MAIOR_IGUAL = 31;
	private static int S_IGUAL = 32;
	private static int S_MENOR = 33;
	private static int S_MENOR_IGUAL = 34;
	private static int S_DIFERENTE = 35;
	private static int S_MAIS = 36;
	private static int S_MENOS = 37;
	private static int S_MULTIPLICACAO = 38;
	private static int S_OU = 20;
	private static int S_E = 19;
	private static int S_DIV = 18;
	private static int S_NUMERO = 23;
	private static int S_NAO = 21;
	private static int S_ABRE_PARENTESES = 28;
	private static int S_FECHA_PARENTESES = 29;
	private static int S_VERDADEIRO = 14;
	private static int S_FALSO = 15;
	private static int S_ATRIBUICAO = 24;

	public static boolean isSPrograma(int idSimbolo) {
		return idSimbolo == S_PROGRAMA;
	}

	public static boolean isSIdentificador(int idSimbolo) {
		return idSimbolo == S_IDENTIFICADOR;
	}

	public static boolean isSPontoEVirgula(int idSimbolo) {
		return idSimbolo == S_PONTO_E_VIRGULA;
	}

	public static boolean isSPonto(int idSimbolo) {
		return idSimbolo == S_PONTO;
	}

	public static boolean isSVar(int idSimbolo) {
		return idSimbolo == S_VAR;
	}

	public static boolean isSDoisPontos(int idSimbolo) {
		return idSimbolo == S_DOIS_PONTOS;
	}

	public static boolean isSVirgula(int idSimbolo) {
		return idSimbolo == S_VIRGULA;
	}

	public static boolean isSInteiro(int idSimbolo) {
		return idSimbolo == S_INTEIRO;
	}

	public static boolean isSBooleano(int idSimbolo) {
		return idSimbolo == S_BOOLEANO;
	}

	public static boolean isSProcedimento(int idSimbolo) {
		return idSimbolo == S_PROCEDIMENTO;
	}

	public static boolean isSFuncao(int idSimbolo) {
		return idSimbolo == S_FUNCAO;
	}

	public static boolean isSInicio(int idSimbolo) {
		return idSimbolo == S_INICIO;
	}

	public static boolean isSFim(int idSimbolo) {
		return idSimbolo == S_FIM;
	}

	public static boolean isSSe(int idSimbolo) {
		return idSimbolo == S_SE;
	}

	public static boolean isSEnquanto(int idSimbolo) {
		return idSimbolo == S_ENQUANTO;
	}

	public static boolean isSFaca(int idSimbolo) {
		return idSimbolo == S_FACA;
	}

	public static boolean isSLeia(int idSimbolo) {
		return idSimbolo == S_LEIA;
	}

	public static boolean isSEscreva(int idSimbolo) {
		return idSimbolo == S_ESCREVA;
	}

	public static boolean isSEntao(int idSimbolo) {
		return idSimbolo == S_ENTAO;
	}

	public static boolean isSSenao(int idSimbolo) {
		return idSimbolo == S_SENAO;
	}

	public static boolean isSMaior(int idSimbolo) {
		return idSimbolo == S_MAIOR;
	}

	public static boolean isSMaiorIgual(int idSimbolo) {
		return idSimbolo == S_MAIOR_IGUAL;
	}

	public static boolean isSIgual(int idSimbolo) {
		return idSimbolo == S_IGUAL;
	}

	public static boolean isSMenor(int idSimbolo) {
		return idSimbolo == S_MENOR;
	}

	public static boolean isSMenorIgual(int idSimbolo) {
		return idSimbolo == S_MENOR_IGUAL;
	}

	public static boolean isSDiferente(int idSimbolo) {
		return idSimbolo == S_DIFERENTE;
	}

	public static boolean isSMais(int idSimbolo) {
		return idSimbolo == S_MAIS;
	}

	public static boolean isSMenos(int idSimbolo) {
		return idSimbolo == S_MENOS;
	}

	public static boolean isSMultiplicacao(int idSimbolo) {
		return idSimbolo == S_MULTIPLICACAO;
	}

	public static boolean isSOu(int idSimbolo) {
		return idSimbolo == S_OU;
	}

	public static boolean isSE(int idSimbolo) {
		return idSimbolo == S_E;
	}

	public static boolean isSDivisao(int idSimbolo) {
		return idSimbolo == S_DIV;
	}

	public static boolean isSNumero(int idSimbolo) {
		return idSimbolo == S_NUMERO;
	}

	public static boolean isSNao(int idSimbolo) {
		return idSimbolo == S_NAO;
	}

	public static boolean isSAbreParenteses(int idSimbolo) {
		return idSimbolo == S_ABRE_PARENTESES;
	}

	public static boolean isSFechaParenteses(int idSimbolo) {
		return idSimbolo == S_FECHA_PARENTESES;
	}

	public static boolean isSVerdadeiro(int idSimbolo) {
		return idSimbolo == S_VERDADEIRO;
	}

	public static boolean isSFalso(int idSimbolo) {
		return idSimbolo == S_FALSO;
	}
	
	public static boolean isSAtribuicao(int idSimbolo) {
		return idSimbolo == S_ATRIBUICAO;
	}

	private Simbolos() {
	}

}
