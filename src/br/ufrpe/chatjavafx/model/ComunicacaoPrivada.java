package br.ufrpe.chatjavafx.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import br.ufrpe.chatjavafx.view.Alerta;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ComunicacaoPrivada extends Thread {
	public static ArrayList<BufferedWriter> privados = new ArrayList<>();
	private String nome;
	private Socket con;
	private InputStream in;
	private InputStreamReader inr;
	private BufferedReader bfr;
	private boolean privado;
	
	public ComunicacaoPrivada(Socket con, ServerSocket server) throws NumberFormatException, IOException {

		// server = new ServerSocket(Integer.parseInt("12345"));
		InetAddress inet = server.getInetAddress();

		System.out.println("Endereço do host " + inet.getHostAddress());
		System.out.println("Nome do Host " + inet.getHostName());
		System.out.println("Porta:" + server.getLocalPort());
		System.out.println("\n" + server.toString());

		// clientes = new ArrayList<BufferedWriter>();

		this.con = con;
		try {
			in = con.getInputStream();
			inr = new InputStreamReader(in);
			bfr = new BufferedReader(inr);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void run() {
		try {

			String msgCompleta;
			OutputStream ou = this.con.getOutputStream();
			Writer ouw = new OutputStreamWriter(ou);
			BufferedWriter bfw = new BufferedWriter(ouw);
		
			nome = msgCompleta = bfr.readLine();

			while (!"Sair".equalsIgnoreCase(msgCompleta) && msgCompleta != null) {
				msgCompleta = bfr.readLine();
				send(bfw, msgCompleta);

			}

		} catch (SocketException e) {
			Alerta alerta = Alerta.getInstace(Alert.AlertType.WARNING);
			alerta.alertar(Alert.AlertType.WARNING, "Atenção", "Alguém foi desconectado", "Atenção");

		} catch (IOException e) {
			Alerta alerta = Alerta.getInstace(Alert.AlertType.WARNING);
			alerta.alertar(AlertType.WARNING, "Atenção", "Erro ao carregar arquivo", "Atenção");
		}
	}

	public void send(BufferedWriter bwSaida, String msg) {
		try {
			BufferedWriter bwS;
			for (BufferedWriter bw : privados) {
				bwS = (BufferedWriter) bw;
				if (!(bwSaida == bwS)) {
					System.out.println(msg);

					bw.write(msg + "\r\n");

					bw.flush();
				}

			}
		} catch (IOException e) {
			Alerta alerta = Alerta.getInstace(AlertType.NONE);
			alerta.alertar(AlertType.WARNING, "Atenção", "Atenção", "Erro ao tentar carregar o arquivo!");
		}
	}

}
