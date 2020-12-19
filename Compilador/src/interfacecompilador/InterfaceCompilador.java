package interfacecompilador;

import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;

import sintatico.AnalisadorSintatico;

@SuppressWarnings("serial")
public class InterfaceCompilador extends JFrame {

	private File file;
	private JMenuItem abrir;
	private JMenuBar barraMenu;
	private JScrollPane codigoPane;
	private JTextArea codigoTextArea;
	private JButton botaoCompilar;
	private JScrollPane errosPane;
	private JTextArea errosTextArea;
	private JMenuItem fechar;
	private JFileChooser escolherArquivo;
	private JMenu menu;

	public InterfaceCompilador() {
		iniciarComponentes();
	}

	private void iniciarComponentes() {

		escolherArquivo = new JFileChooser();
		codigoPane = new JScrollPane();
		codigoTextArea = new JTextArea();
		errosPane = new JScrollPane();
		errosTextArea = new JTextArea();
		botaoCompilar = new JButton();
		barraMenu = new JMenuBar();
		menu = new JMenu();
		abrir = new JMenuItem();
		fechar = new JMenuItem();

		escolherArquivo.setFileFilter(new MyCustomFilter());

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Compilador");

		codigoTextArea.setColumns(20);
		codigoTextArea.setRows(5);
		codigoTextArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)),
						"Código", 0, 0, new java.awt.Font("Century", 0, 12)));
		codigoTextArea.setEditable(false);
		codigoPane.setViewportView(codigoTextArea);

		errosTextArea.setColumns(20);
		errosTextArea.setRows(5);
		errosTextArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)),
				"Erros", 0, 0, new java.awt.Font("Century", 0, 12)));
		errosTextArea.setEditable(false);
		errosPane.setViewportView(errosTextArea);

		botaoCompilar.setIcon(new ImageIcon(getClass().getResource("icones/play.png")));
		botaoCompilar.setText("Compilar");
		botaoCompilar.setEnabled(false);
		botaoCompilar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				compilarActionPerformed(evt);
			}
		});

		menu.setText("Arquivo");

		abrir.setIcon(new ImageIcon(getClass().getResource("icones/pasta.png")));
		abrir.setText("Abrir");
		abrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				abrirActionPerformed(evt);
			}
		});
		menu.add(abrir);

		fechar.setIcon(new ImageIcon(getClass().getResource("icones/x.jpg")));
		fechar.setText("Fechar");
		fechar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				fecharActionPerformed(evt);
			}
		});
		menu.add(fechar);

		barraMenu.add(menu);

		setJMenuBar(barraMenu);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup().addContainerGap()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(codigoPane, GroupLayout.Alignment.TRAILING,
												GroupLayout.DEFAULT_SIZE, 708, Short.MAX_VALUE)
								.addComponent(errosPane, GroupLayout.Alignment.TRAILING)
								.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
										.addGap(0, 0, Short.MAX_VALUE).addComponent(botaoCompilar)))
				.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addComponent(botaoCompilar)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(codigoPane, GroupLayout.PREFERRED_SIZE, 274, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
				.addComponent(errosPane, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)));

		pack();
	}

	private void abrirActionPerformed(ActionEvent evt) {
		int returnVal = escolherArquivo.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = escolherArquivo.getSelectedFile();
			errosTextArea.setText("");
			try {
				// Ler o arquivo
				codigoTextArea.read(new FileReader(file.getAbsolutePath()), null);
				// para ter acesso "global" ao arquivo
				this.file = file;
				botaoCompilar.setEnabled(true);
			} catch (IOException ex) {
				errosTextArea.setText("Problema ao acessar o arquivo" + file.getAbsolutePath());
			}
		} else {
			errosTextArea.setText("Usuário cancelou a seleção de arquivo");
		}
	}

	private void fecharActionPerformed(ActionEvent evt) {
		codigoTextArea.setText("");
		errosTextArea.setText("");
		botaoCompilar.setEnabled(false);
	}

	private void compilarActionPerformed(ActionEvent evt) {
		try {
			@SuppressWarnings("unused")
			AnalisadorSintatico analisador = new AnalisadorSintatico(file.getAbsolutePath());
		} catch (Exception e) {
			errosTextArea.setText(e.getMessage());
		}
	}

	public static void main(String args[]) {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
		} catch (UnsupportedLookAndFeelException ex) {
			Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new InterfaceCompilador().setVisible(true);
			}
		});
	}

}

class MyCustomFilter extends FileFilter {
	public boolean accept(File file) {
		// Para mostrar arquivos com a extensão lpd
		return file.isDirectory() || file.getAbsolutePath().endsWith(".lpd") || file.getAbsolutePath().endsWith(".txt");
	}

	public String getDescription() {
		// descrição
		return "Arquivos LPD (*.lpd) ou Arquivos txt (*.txt)";
	}
}
