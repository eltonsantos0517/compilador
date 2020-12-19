package lexico;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class Printer extends JFrame {
	public Printer(List<Token> lista) {
		String[] cabecalho = new String[] { "Lexema", "Símbolo", "ID do Símbolo", "Linha", "Coluna Primeiro Caracter",
				"Coluna Ultimo Caracter","Descrição do erro" };
		DefaultTableModel conteudo = new DefaultTableModel(null, cabecalho);
		for (Token token : lista) {
			conteudo.addRow(new String[] { token.getLexema(), token.getSimbolo(), token.getIdSimbolo().toString(),
					token.getLinha().toString(), token.getColunaPrimeiroCaracter().toString(),
					token.getColunaUltimoCaracter().toString(),token.getdescricaoErro() });
		}
		JTable tabela = new JTable(conteudo);
		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(tabela);
		super.add(scroll);
		super.setVisible(true);
		super.setSize(800, 600);

	}

}
