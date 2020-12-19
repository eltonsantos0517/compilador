package tabeladesimbolos;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class PrinterTabelaDeSimbolos extends JFrame {
	public PrinterTabelaDeSimbolos(List<Simbolo> simbolos) {
		String[] cabecalho = new String[] { "Simbolo", "Tipo", "Lexema", "Escopo" ,"Memória"};
		DefaultTableModel conteudo = new DefaultTableModel(null, cabecalho);
		for (Simbolo simbolo : simbolos) {
			conteudo.addRow(new String[] { 
					//Simbolo
					simbolo instanceof Variavel ? "Váriavel" :
					simbolo instanceof Funcao ? "Função" :
					"Procedimento", // caso não seja váriavel ou função, é procedimento
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
					//Memória
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

