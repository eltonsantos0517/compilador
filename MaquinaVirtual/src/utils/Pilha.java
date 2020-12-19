package utils;

public class Pilha {
	private int[] pilha;
	private int topo = -2;
	public Pilha(int tamanhoPilha)
	{
		pilha = new int[tamanhoPilha];
	}
	
	public void setPosition(int posicao, int parametro)
	{
		pilha[posicao] = parametro;
	}
	
	public int getPosition(int posicao)
	{
		return pilha[posicao];
	}
	
	public void incrementaTopo()
	{
		topo++;
	}
	
	public void decrementaTopo()
	{
		topo--;
	}
	
	public int getTopo()
	{
		return topo;
	}

}
