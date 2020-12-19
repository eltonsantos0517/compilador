package tabeladesimbolos;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class PrinterTabelaDeSimbolos extends JFrame {
	public PrinterTabelaDeSimbolos(List<Simbolo> simbolos) {
		String[] cabecalho = new String[] { "Simbolo", "Tipo", "Lexema", "Escopo" ,"Mem�ria"};
		DefaultTableModel conteudo = new DefaultTableModel(null, cabecalho);
		for (Simbolo simbolo : simbolos) {
			conteudo.addRow(new String[] { 
					//Simbolo
					simbolo instanceof Variavel ? "V�riavel" :
					simbolo instanceof Funcao ? "Fun��o" :
					"Procedimento", // caso n�o seja v�riavel ou fun��o, � procedimento
					//Tipo
					//((Variavel)simbolo).getTipo() != null ? ((Variavel)simbolo).getTipo() : ""
					simbolo instanceof Variavel ? ((Variavel)simbolo).getTipo() : 
					simbolo instanceof Funcao ?   ((Funcao)simbolo).getTipo() :	"Procedimento",
					//Lexema
					simbolo.getLexema(),
					//Escopo
					simbolo instanceof Procedimento ? ((Procedimento)simbolo).getEscopo().toString() :
					simbolo instanceof Funcao ? ((Funcao)simbolo).getEscopo().toString() :
						"null",
					//Mem�ria
					simbolo.getMemoria() != null ? simbolo.getMemoria().toString() : "null",
							

								
			});
		}
		JTable tabela = new JTable(conteudo);
		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(tabela);
		super.add(scroll);
		super.setVisible(true);
		super.setSize(800, 600);

	}

}

