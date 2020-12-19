package visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import utils.Pilha;
import utils.StringUtils;

public class MaquinaVirtual extends JFrame {

	private static final String[] CABECALHO_PILHA = new String[] { "Posição", "Pilha" };
	private static final String[] CABECALHO_CODIGO = new String[] { "Linha", "Comandos" };
	private JButton botaoProximaInstrucao;
	private JLabel maquinaVirtualLabel;
	private JPanel panelPrincipal;
	private JScrollPane panelResultado;
	private JLabel pilhaLabel;
	private JLabel codigoLabel;
	private JScrollPane pilhaScrollPane;
	private JTextArea resultado;
	private JTable tabelaLinhaComandos;
	private JScrollPane tabelaLinhaComandosScrollPane;
	private JTable tabelaPilha;

	public MaquinaVirtual(boolean isDebug) throws InterruptedException {
		initComponents(isDebug);
	}

	public void initComponents(boolean isDebug) throws InterruptedException {

		panelPrincipal = new JPanel();
		tabelaLinhaComandosScrollPane = new JScrollPane();
		tabelaLinhaComandos = new JTable();
		pilhaScrollPane = new JScrollPane();
		tabelaPilha = new JTable();
		botaoProximaInstrucao = new JButton();
		panelResultado = new JScrollPane();
		resultado = new JTextArea();
		maquinaVirtualLabel = new JLabel();
		pilhaLabel = new JLabel();
		codigoLabel = new JLabel();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setMaximumSize(new Dimension(1120, 600));
		setMinimumSize(new Dimension(1120, 600));
		setPreferredSize(new Dimension(1120, 600));
		setResizable(false);

		tabelaLinhaComandos.setModel(new DefaultTableModel(null, new String[] { "Linha", "Comando" }));
		tabelaLinhaComandos.setEnabled(false);
		tabelaLinhaComandosScrollPane.setViewportView(tabelaLinhaComandos);

		tabelaPilha.setModel(new DefaultTableModel(null, new String[] { "Posição", "Conteudo" }));
		tabelaPilha.setCellSelectionEnabled(true);
		tabelaPilha.setEnabled(false);
		pilhaScrollPane.setViewportView(tabelaPilha);

		botaoProximaInstrucao.setText("Próxima Instrução");
		botaoProximaInstrucao.setEnabled(isDebug);
		botaoProximaInstrucao.setVisible(false);
		
		resultado.setColumns(20);
		resultado.setRows(5);
		resultado.setBorder(BorderFactory.createTitledBorder(null, "Resultado", TitledBorder.CENTER,
				TitledBorder.DEFAULT_POSITION, new Font("Century", 0, 14))); // NOI18N
		resultado.setEnabled(false);
		panelResultado.setViewportView(resultado);

		maquinaVirtualLabel.setFont(new Font("Century", 1, 18)); // NOI18N
		maquinaVirtualLabel.setHorizontalAlignment(SwingConstants.CENTER);
		maquinaVirtualLabel.setText("MaquinaVirtual LPD");
		maquinaVirtualLabel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));

		pilhaLabel.setFont(new Font("Century", 0, 14)); // NOI18N
		pilhaLabel.setHorizontalAlignment(SwingConstants.CENTER);
		pilhaLabel.setText("Pilha");

		codigoLabel.setFont(new Font("Century", 0, 14)); // NOI18N
		codigoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		codigoLabel.setText("Código");

		GroupLayout panelPrincipalLayout = new GroupLayout(panelPrincipal);
		panelPrincipal.setLayout(panelPrincipalLayout);
		panelPrincipalLayout.setHorizontalGroup(panelPrincipalLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(panelPrincipalLayout.createSequentialGroup().addGap(38, 38, 38)
						.addComponent(tabelaLinhaComandosScrollPane, GroupLayout.PREFERRED_SIZE, 375,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
						.addGroup(panelPrincipalLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(GroupLayout.Alignment.TRAILING,
										panelPrincipalLayout.createSequentialGroup()
												.addComponent(botaoProximaInstrucao, GroupLayout.PREFERRED_SIZE, 200,
														GroupLayout.PREFERRED_SIZE)
												.addGap(40, 40, 40))
								.addGroup(GroupLayout.Alignment.TRAILING,
										panelPrincipalLayout.createSequentialGroup()
												.addComponent(panelResultado, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)))
						.addComponent(pilhaScrollPane, GroupLayout.PREFERRED_SIZE, 375, GroupLayout.PREFERRED_SIZE)
						.addGap(32, 32, 32))
				.addGroup(GroupLayout.Alignment.TRAILING,
						panelPrincipalLayout.createSequentialGroup().addGap(110, 110, 110)
								.addComponent(codigoLabel, GroupLayout.PREFERRED_SIZE, 183, GroupLayout.PREFERRED_SIZE)
								.addGap(119, 119, 119).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(maquinaVirtualLabel, GroupLayout.PREFERRED_SIZE, 245,
										GroupLayout.PREFERRED_SIZE)
								.addGap(120, 120, 120)
								.addComponent(pilhaLabel, GroupLayout.PREFERRED_SIZE, 183, GroupLayout.PREFERRED_SIZE)
								.addGap(119, 119, 119)));
		panelPrincipalLayout
				.setVerticalGroup(panelPrincipalLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(panelPrincipalLayout.createSequentialGroup().addGap(257, 257, 257)
								.addComponent(botaoProximaInstrucao, GroupLayout.PREFERRED_SIZE, 23,
										GroupLayout.PREFERRED_SIZE)
						.addGap(26, 26, 26)
						.addComponent(panelResultado, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGroup(panelPrincipalLayout.createSequentialGroup().addGap(11, 11, 11)
						.addGroup(panelPrincipalLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(pilhaLabel, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
								.addComponent(maquinaVirtualLabel, GroupLayout.PREFERRED_SIZE, 38,
										GroupLayout.PREFERRED_SIZE)
						.addComponent(codigoLabel, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(panelPrincipalLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(pilhaScrollPane, GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)
								.addComponent(tabelaLinhaComandosScrollPane))
						.addContainerGap()));

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout
								.createSequentialGroup().addComponent(panelPrincipal, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGap(0, 4, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup()
				.addComponent(panelPrincipal, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addContainerGap()));


		pack();
	}
	public void printCodigo(List<String> comandos) {

		DefaultTableModel conteudo = new DefaultTableModel(null, CABECALHO_CODIGO);

		int i = -1;
		for (String comando : comandos) {
			i++;
			conteudo.addRow(new String[] { StringUtils.intToString(i), comando.toUpperCase() });
		}
		tabelaLinhaComandos.setModel(conteudo);

	}

	public void printPilha(Pilha p) {
		DefaultTableModel conteudo = new DefaultTableModel(null, CABECALHO_PILHA);
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (int i = p.getTopo(); i >= 0; i--) {
			conteudo.addRow(new String[] { StringUtils.intToString(i), StringUtils.intToString(p.getPosition(i)) });
		}
		tabelaPilha.setModel(conteudo);

	}

	public void printConsole(String saida) {
		resultado.append(saida+"  ");
	}

	public void aguardarTecla(final int linha) {

		tabelaLinhaComandos.setRowSelectionInterval(linha, linha);
		JOptionPane.showMessageDialog(null, "Próxima instrução");
		tabelaLinhaComandos.clearSelection();

	}

	public void encerrarJanela() {
		super.dispose();
	}
}
