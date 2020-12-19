package lexico;

public class Token {
	private Integer idSimbolo;
	private String lexema;
	private String simbolo;
	private Integer linha;
	private Integer colunaPrimeiroCaracter;
	private Integer colunaUltimoCaracter;
	private String descricaoErro;

	Token() {
	}

	public Token(Token t) {
		this.idSimbolo = t.idSimbolo;
		this.lexema = t.lexema;
		this.simbolo = t.simbolo;
		this.linha = t.linha;
		this.colunaPrimeiroCaracter = t.colunaPrimeiroCaracter;
		this.colunaUltimoCaracter = t.colunaUltimoCaracter;
		this.descricaoErro = t.descricaoErro;
	}

	public Integer getIdSimbolo() {
		return idSimbolo;
	}

	public void setIdSimbolo(Integer id_simbolo) {
		this.idSimbolo = id_simbolo;
	}

	public String getLexema() {
		return lexema;
	}

	public void setLexema(String lexema) {
		this.lexema = lexema;
	}

	public String getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}

	public Integer getLinha() {
		return linha;
	}

	public void setLinha(Integer linha) {
		this.linha = linha;
	}

	public Integer getColunaPrimeiroCaracter() {
		return colunaPrimeiroCaracter;
	}

	public void setColunaPrimeiroCaracter(Integer colunaPrimeiroCaracter) {
		this.colunaPrimeiroCaracter = colunaPrimeiroCaracter;
	}

	public Integer getColunaUltimoCaracter() {
		return colunaUltimoCaracter;
	}

	public void setColunaUltimoCaracter(Integer colunaUltimoCaracter) {
		this.colunaUltimoCaracter = colunaUltimoCaracter;
	}

	public String getdescricaoErro() {
		return descricaoErro;
	}

	public void setdescricaoErro(String descricaoErro) {
		this.descricaoErro = descricaoErro;
	}


}
