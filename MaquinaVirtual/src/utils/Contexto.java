package utils;

public class Contexto {

	private Pilha pilha;
	private int pc;

	public Contexto(int tamanhoPilha) {
		this.pilha = new Pilha(tamanhoPilha);
		this.pc = 0;
	}

	public Pilha getPilha() {
		return pilha;
	}

	public void setPilha(Pilha pilha) {
		this.pilha = pilha;
	}

	public int pc() {
		return pc;
	}

	public void incrementarPC() {
		this.pc++;
	}
	
	public void setPc(int linha){
		this.pc = linha;
	}

}
