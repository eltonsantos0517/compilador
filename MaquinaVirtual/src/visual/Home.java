/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visual;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import utils.Maquina;

public class Home extends JFrame {
	private JMenuBar barraMenu;
	private JFileChooser escolherArquivo;
	private JButton executar;
	private JButton executarModoDebug;
	private JPanel homeJPanel;
	private JMenu homeMenu;
	private JMenuItem menuAbrir;
	private JMenuItem menuFechar;
	private File file;
	
	public final static int TAMANHO_PILHA = 10000;
	public Home() {
		initComponents();
	}

	private void initComponents() {

		escolherArquivo = new JFileChooser();
		homeJPanel = new JPanel();
		executar = new JButton();
		executarModoDebug = new JButton();
		barraMenu = new JMenuBar();
		homeMenu = new JMenu();
		menuAbrir = new JMenuItem();
		menuFechar = new JMenuItem();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Compilador");
		setMaximumSize(new java.awt.Dimension(248, 177));
		setMinimumSize(new java.awt.Dimension(248, 177));
		setName("homeJFrame"); // NOI18N
		setResizable(false);

		homeJPanel.setBorder(BorderFactory.createTitledBorder(null, "Escolha o modo de execução", TitledBorder.CENTER,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Century", 0, 14))); // NOI18N

		executar.setText("Executar");
		executar.setEnabled(false);
		executar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					new Maquina(file.getAbsolutePath(), TAMANHO_PILHA, false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		executarModoDebug.setText("Executar Modo Debug");
		executarModoDebug.setEnabled(false);
		executarModoDebug.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
	
				try {
					new Maquina(file.getAbsolutePath(), TAMANHO_PILHA, true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

		GroupLayout homeJPanelLayout = new GroupLayout(homeJPanel);
		homeJPanel.setLayout(homeJPanelLayout);
		homeJPanelLayout.setHorizontalGroup(homeJPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(homeJPanelLayout.createSequentialGroup().addContainerGap().addComponent(executar)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
						.addComponent(executarModoDebug).addContainerGap()));
		homeJPanelLayout.setVerticalGroup(homeJPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(homeJPanelLayout.createSequentialGroup().addGap(42, 42, 42)
						.addGroup(homeJPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(executar).addComponent(executarModoDebug))
						.addContainerGap(80, Short.MAX_VALUE)));

		homeMenu.setText("Arquivo");

		menuAbrir.setText("Abrir");
		menuAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				menuAbrirActionPerformed(evt);
			}
		});
		homeMenu.add(menuAbrir);

		menuFechar.setText("Fechar");
		homeMenu.add(menuFechar);

		barraMenu.add(homeMenu);

		setJMenuBar(barraMenu);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(homeJPanel,
				GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(homeJPanel,
						GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));

		pack();
	}// </editor-fold>

	private void menuAbrirActionPerformed(ActionEvent evt) {
		int returnVal = escolherArquivo.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = escolherArquivo.getSelectedFile();
			this.file = file;
			executar.setEnabled(true);
			executarModoDebug.setEnabled(true);
		}
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (UnsupportedLookAndFeelException ex) {
			Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Home().setVisible(true);
			}
		});
	}
	

}
