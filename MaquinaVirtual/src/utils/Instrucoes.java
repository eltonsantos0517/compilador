package utils;

import javax.swing.JOptionPane;

public class Instrucoes {
	private Contexto contexto;

	public Instrucoes(Contexto contexto) {
		this.contexto = contexto;
	}

	private Pilha pilha() {
		return contexto.getPilha();
	}

	private int topo() {
		return contexto.getPilha().getTopo();
	}

	public void start() {
		pilha().incrementaTopo();
	}

	public void ldc(int k) {
		pilha().incrementaTopo();
		pilha().setPosition(topo(), k);
	}

	public void ldv(int n) {
		pilha().incrementaTopo();
		pilha().setPosition(topo(), pilha().getPosition(n));
	}

	public void add() {
		int conteudoTopo = pilha().getPosition(topo());
		int conteudoAnteTopo = pilha().getPosition(topo() - 1);
		pilha().setPosition(topo() - 1, conteudoAnteTopo + conteudoTopo);
		pilha().decrementaTopo();

	}

	public void sub() {
		int conteudoTopo = pilha().getPosition(topo());
		int conteudoAnteTopo = pilha().getPosition(topo() - 1);
		pilha().setPosition(topo() - 1, conteudoAnteTopo - conteudoTopo);
		pilha().decrementaTopo();

	}

	public void mult() {
		int conteudoTopo = pilha().getPosition(topo());
		int conteudoAnteTopo = pilha().getPosition(topo() - 1);
		pilha().setPosition(topo() - 1, conteudoAnteTopo * conteudoTopo);
		pilha().decrementaTopo();

	}

	public void divi() {
		int conteudoTopo = pilha().getPosition(topo());
		int conteudoAnteTopo = pilha().getPosition(topo() - 1);
		pilha().setPosition(topo() - 1, conteudoAnteTopo / conteudoTopo);
		pilha().decrementaTopo();

	}

	public void inv() {
		int conteudoTopo = pilha().getPosition(topo());
		pilha().setPosition(topo(), conteudoTopo * -1);

	}

	public void and() {
		int conteudoTopo = pilha().getPosition(topo());
		int conteudoAnteTopo = pilha().getPosition(topo() - 1);
		if (conteudoAnteTopo == 1 && conteudoTopo == 1) {
			pilha().setPosition(topo() - 1, 1);
		} else {
			pilha().setPosition(topo() - 1, 0);
		}

		pilha().decrementaTopo();

	}

	public void or() {
		int conteudoTopo = pilha().getPosition(topo());
		int conteudoAnteTopo = pilha().getPosition(topo() - 1);
		if (conteudoAnteTopo == 1 || conteudoTopo == 1) {
			pilha().setPosition(topo() - 1, 1);
		} else {
			pilha().setPosition(topo() - 1, 0);
		}

		pilha().decrementaTopo();
	}

	public void neg() {
		int conteudoTopo = pilha().getPosition(topo());
		pilha().setPosition(topo(), 1 - conteudoTopo);
	}

	public void cme() {
		int conteudoTopo = pilha().getPosition(topo());
		int conteudoAnteTopo = pilha().getPosition(topo() - 1);
		if (conteudoAnteTopo < conteudoTopo) {
			pilha().setPosition(topo() - 1, 1);
		} else {
			pilha().setPosition(topo() - 1, 0);
		}
		pilha().decrementaTopo();
	}

	public void cma() {
		int conteudoTopo = pilha().getPosition(topo());
		int conteudoAnteTopo = pilha().getPosition(topo() - 1);
		if (conteudoAnteTopo > conteudoTopo) {
			pilha().setPosition(topo() - 1, 1);
		} else {
			pilha().setPosition(topo() - 1, 0);
		}
		pilha().decrementaTopo();
	}

	public void ceq() {
		int conteudoTopo = pilha().getPosition(topo());
		int conteudoAnteTopo = pilha().getPosition(topo() - 1);
		if (conteudoAnteTopo == conteudoTopo) {
			pilha().setPosition(topo() - 1, 1);
		} else {
			pilha().setPosition(topo() - 1, 0);
		}
		pilha().decrementaTopo();
	}

	public void cdif() {
		int conteudoTopo = pilha().getPosition(topo());
		int conteudoAnteTopo = pilha().getPosition(topo() - 1);
		if (conteudoAnteTopo != conteudoTopo) {
			pilha().setPosition(topo() - 1, 1);
		} else {
			pilha().setPosition(topo() - 1, 0);
		}
		pilha().decrementaTopo();
	}

	public void cmeq() {
		int conteudoTopo = pilha().getPosition(topo());
		int conteudoAnteTopo = pilha().getPosition(topo() - 1);
		if (conteudoAnteTopo <= conteudoTopo) {
			pilha().setPosition(topo() - 1, 1);
		} else {
			pilha().setPosition(topo() - 1, 0);
		}
		pilha().decrementaTopo();
	}

	public void cmaq() {
		int conteudoTopo = pilha().getPosition(topo());
		int conteudoAnteTopo = pilha().getPosition(topo() - 1);
		if (conteudoAnteTopo >= conteudoTopo) {
			pilha().setPosition(topo() - 1, 1);
		} else {
			pilha().setPosition(topo() - 1, 0);
		}
		pilha().decrementaTopo();
	}

	public void hlt() throws Exception {
		throw new Exception("Fim da execução");
	}

	public void str(int n) {
		int conteudoTopo = pilha().getPosition(topo());
		pilha().setPosition(n, conteudoTopo);
		pilha().decrementaTopo();
	}

	public void jmp(int linha) {
		contexto.setPc(linha);
	}

	public void jmpf(int linha) {
		int conteudoTopo = pilha().getPosition(topo());
		if (conteudoTopo == 0) {
			contexto.setPc(linha);
		}
		pilha().decrementaTopo();
	}

	public void rd() {
		
		int entrada = 0; 
		boolean deuCerto = false;
		
		
		while(!deuCerto){
			try{
				entrada = Integer.parseInt(JOptionPane.showInputDialog("Digite um numero:"));
				deuCerto = true;
			}catch(NumberFormatException e){
				deuCerto = false;
			}
		}
		
		pilha().incrementaTopo();
		pilha().setPosition(topo(), entrada);
	}

	public String prn() {
		int conteudoTopo = pilha().getPosition(topo());
		String retorno = StringUtils.intToString(conteudoTopo);
		pilha().decrementaTopo();
		return retorno;
	}

	public void alloc(String mn) {
		int k;
		String[] parametros = mn.split(",");

		int m = Integer.parseInt(parametros[0]);
		int n = Integer.parseInt(parametros[1]);

		for (k = 0; k <= (n - 1); k++) {
			pilha().incrementaTopo();
			pilha().setPosition(topo(), pilha().getPosition(m + k));
		}
	}

	public void dalloc(String mn) {
		int k;
		String[] parametros = mn.split(",");

		int m = Integer.parseInt(parametros[0]);
		int n = Integer.parseInt(parametros[1]);

		for (k = (n - 1); k >= 0; k--) {
			pilha().setPosition(m + k, pilha().getPosition(topo()));
			pilha().decrementaTopo();

		}
	}

	public void call(int linha) {
		pilha().incrementaTopo();
		pilha().setPosition(topo(), contexto.pc());
		contexto.setPc(linha);
	}

	public void retorna() {
		int conteudoTopo = pilha().getPosition(topo());
		int linha = conteudoTopo;
		contexto.setPc(linha);
		pilha().decrementaTopo();
	}

	public void returnf(String mn) {

		int retornoFuncao = pilha().getPosition(topo());
		pilha().decrementaTopo();
		dalloc(mn);
		int linhaRetorno = pilha().getPosition(topo());
		pilha().setPosition(topo(), retornoFuncao);
		contexto.setPc(linhaRetorno);
	}
}
