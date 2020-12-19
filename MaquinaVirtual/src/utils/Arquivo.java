package utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Arquivo {

	private List<String> comandos;
	public Arquivo(String path) throws Exception {
		try {
			comandos = new ArrayList<>();
			BufferedReader leitor;
			leitor = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			String comando;
			
				comando = leitor.readLine();
			while (comando != null) {
				comandos.add(new String(comando).toLowerCase());
				comando = leitor.readLine();
			}
			leitor.close();
		} catch (IOException e) {
			throw new Exception("Arquivo de instrucoes nao encontrado");
		}
		
	}

	public List<String> getComandos() {
		return comandos;
	}

}
