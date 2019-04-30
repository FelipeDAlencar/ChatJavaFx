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
import java.util.ArrayList;

public class ServidorPrivado extends Thread{
	public static ArrayList<BufferedWriter> clientes = new ArrayList<>();
//	private static ServerSocket server;
	private String nome;
	private Socket con;
	private InputStream in;
	private InputStreamReader inr;
	private BufferedReader bfr;

	
	public ServidorPrivado(Socket con, ServerSocket server) throws NumberFormatException, IOException {

		//server = new ServerSocket(Integer.parseInt("12345"));
		System.out.println("Aqui" +  server);
		InetAddress inet = server.getInetAddress();

		System.out.println("Endereço do host " + inet.getHostAddress());
		System.out.println("Nome do Host " + inet.getHostName());
		System.out.println("Porta:" + server.getLocalPort());
		System.out.println("\n" + server.toString());
		
		//clientes = new ArrayList<BufferedWriter>();
		
		
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
		System.out.println("RUN");
		try {

			String msgCompleta;
			OutputStream ou = this.con.getOutputStream();
			Writer ouw = new OutputStreamWriter(ou);
			BufferedWriter bfw = new BufferedWriter(ouw);
			clientes.add(bfw);
			System.out.println(clientes);
			nome = msgCompleta = bfr.readLine();

			while (!"Sair".equalsIgnoreCase(msgCompleta) && msgCompleta != null) {
				msgCompleta = bfr.readLine();
				sendToAll(bfw, msgCompleta);

			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void sendToAll(BufferedWriter bwSaida, String msg) throws IOException {
		BufferedWriter bwS;
		System.out.println(clientes);
		for (BufferedWriter bw : clientes) {
			bwS = (BufferedWriter) bw;
			if (!(bwSaida == bwS)) {
				System.out.println(msg);

				bw.write(msg + "\r\n");

				bw.flush();
			}

		}
	}


}
