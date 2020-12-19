package utils;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import visual.MaquinaVirtual;

public class Maquina {

	public static boolean DEBUG = true;

	private Contexto contexto;
	private boolean falha;
	private Class<?> instrucoesClass;
	private Instrucoes instrucoes;
	private Arquivo arq;
	private MaquinaVirtual janela;
	private boolean isDebug;



	public Maquina(String arquivo, int tamanhoPilha, boolean isDebug) throws Exception {
		
		this.isDebug = isDebug;
		initJanela();
		this.contexto = new Contexto(tamanhoPilha);
		this.falha = false;

		
		try {
			instrucoesClass = Class.forName("utils.Instrucoes");
		} catch (ClassNotFoundException e1) {
			falha = true;
			throw new Exception("Classe de instrucoes não encontrada");
		}
		instrucoes = new Instrucoes(contexto);
		arq = new Arquivo(arquivo);

		janela.printCodigo(arq.getComandos());
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				try {
					executar(new ArrayList<Integer>());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		

	}

	public void executar(final List<Integer> breakPointsLines) throws Exception {
		try {
			while (true) {
				String comando;
				comando = arq.getComandos().get(contexto.pc());

				if ("hlt".equals(comando)) {
					String[] comandoPartes = { "hlt" };
					log(comandoPartes);
					janela.printPilha(contexto.getPilha());
					break;
				}

				janela.printPilha(contexto.getPilha());

				if (isDebug) {
					// para esperar a execucao do comando
					janela.aguardarTecla(contexto.pc());
				}

				String[] comandoPartes = comando.split(" ");
				log(comandoPartes);
				if (comandoPartes[0].equalsIgnoreCase("return")) {
					comandoPartes[0] = "retorna";
				}
				
				if(comando.toUpperCase().contains("NULL".toUpperCase())){
					System.out.println(comandoPartes[0]);
				}else{
					printConsole(chamarMetodo(instrucoesClass, comandoPartes, instrucoes));
				}

				contexto.incrementarPC();
			}

		} catch (IndexOutOfBoundsException e) {
			falha = true;
			throw new Exception("Nao existe comando HLT");
		} catch (NoSuchMethodException e3) {
			falha = true;
			throw new Exception("Instrucao invalida - Linha: " + (contexto.pc() + 1));
		} catch (SecurityException e4) {
			falha = true;
			e4.printStackTrace();
		} catch (IllegalArgumentException e5) {
			falha = true;
			e5.printStackTrace();
		} catch (InvocationTargetException e6) {
			falha = true;
			e6.printStackTrace();
		} catch (IllegalAccessException e7) {
			falha = true;
			e7.printStackTrace();
		} finally {
			if (falha) {
				janela.encerrarJanela();
			}
		}
	}

	private void log(String[] parts) {
		System.out.print("Comando:");
		for (String part : parts) {
			System.out.print("\t" + part);
		}
		System.out.println();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object chamarMetodo(Class instrucoesClass, String[] comandoPartes, Instrucoes instrucoes)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		Method method = null;
		Class<?>[] paramTypes = null;
		if (comandoPartes.length == 1) {
			method = instrucoesClass.getMethod(comandoPartes[0], paramTypes);
		} else if (comandoPartes.length == 2) {
			paramTypes = new Class<?>[1];
			if (comandoPartes[1].startsWith("L")) {
				paramTypes[0] = Integer.TYPE;
			} else if (comandoPartes[1].contains(",")) {
				paramTypes[0] = String.class;
			} else {
				paramTypes[0] = Integer.TYPE;
			}
			
			method = instrucoesClass.getMethod(comandoPartes[0], paramTypes);
		} 

		Object[] arguments = null;

		if (comandoPartes.length == 1) {
			return method.invoke(instrucoes, arguments);
		} else {
			arguments = new Object[1];
			if (comandoPartes[1].toUpperCase().startsWith("L")) {
				arguments[0] = procuraLinhaLabel(comandoPartes[1]);
			} else if (comandoPartes[1].contains(",")) {
				arguments[0] = comandoPartes[1];
			} else {
				arguments[0] = Integer.parseInt(comandoPartes[1]);
			}

		}
		return method.invoke(instrucoes, arguments);
	}

	private void printConsole(Object saida) {
		if (saida != null) {
			janela.printConsole(saida.toString());
		}
	}

	public int procuraLinhaLabel(String label){
		for(int i = 0; i<arq.getComandos().size(); i++){
			if(arq.getComandos().get(i).toUpperCase().startsWith(label.toUpperCase())){
				return i;
			}
		}
		return -1;
	}
	
	private void initJanela() throws InterruptedException{
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(MaquinaVirtual.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			Logger.getLogger(MaquinaVirtual.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(MaquinaVirtual.class.getName()).log(Level.SEVERE, null, ex);
		} catch (UnsupportedLookAndFeelException ex) {
			Logger.getLogger(MaquinaVirtual.class.getName()).log(Level.SEVERE, null, ex);
		}
		

		janela = new MaquinaVirtual(isDebug);
		janela.setVisible(true);
		
	}
	
}
