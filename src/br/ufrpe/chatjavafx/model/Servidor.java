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

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import br.ufrpe.chatjavafx.view.Alerta;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Servidor extends Thread {

	public static ArrayList<BufferedWriter> clientes = new ArrayList<>();
	// private static ServerSocket server;
	private String nome;
	private Socket con;
	private InputStream in;
	private InputStreamReader inr;
	private BufferedReader bfr;
	
	public Servidor(Socket con, ServerSocket server) throws NumberFormatException, IOException {

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
			clientes.add(bfw);
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
			for (BufferedWriter bw : clientes) {
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

	public static void main(String[] args) {

		// try {
		//
		// while (true) {
		// System.out.println("Aguardando conexão...");
		// Socket con = server.accept();
		// System.out.println("Cliente conectado...");
		// Thread t = new Servidor(con);
		// t.start();
		// }
		//
		// } catch (Exception e) {
		//
		// e.printStackTrace();
		// }
	}
}
