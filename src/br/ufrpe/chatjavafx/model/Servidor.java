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
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import br.ufrpe.chatjavafx.model.dao.DAOMsgDaSala;
import br.ufrpe.chatjavafx.model.dao.DAOUsuario;
import br.ufrpe.chatjavafx.view.Alerta;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Servidor extends Thread {

	private static final String LOGANDO = "--LOGANDO--";
	private static final String SUCESSO = "--SUCESSO--";
	private static final String ENTROU_NA_SALA = "--ENTROU--";
	public static final String MSG_PRIVADA = "MSG_PRIVADA";
	public static final String REQUISITAR_PRIVADO = "REQUISITAR_PRIVADO";
	private static final String CASDATRAR = "--CASDATRAR--";
	private static final String LOGIN_ACEITO = "--LOGIN_ACEITO--";
	public static final String DIGITANDO = "--digitando--";
	public static final String NAO_DIGITANDO = "--nao_digitando--";
	public static final String SAIR = "--SAIR--";
	public static final String ULTIMO_ONLINE = "ULTIMO_ONLINE";
	public static final String RECUPERAR_MENSAGENS_OFFLINE = "RECUPERAR_MENSAGENS_OFFLINE";

	public static ArrayList<BufferedWriter> clientes = new ArrayList<>();
	// private static ServerSocket server;
	private String nome;
	private Socket con;
	private InputStream in;
	private InputStreamReader inr;
	private BufferedReader bfr;
	private DAOUsuario daoUsuario;
	private DAOMsgDaSala daoMsgDaSala;

	private static Map<String, BufferedWriter> mapaDeCliente = new HashMap<>();
	public static ArrayList<String> clientesEntraram = new ArrayList<>();

	public Servidor(Socket con, ServerSocket server) throws NumberFormatException, IOException {
		daoUsuario = DAOUsuario.getInstance();
		daoMsgDaSala = DAOMsgDaSala.getInstance();
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

				if (msgCompleta.contains(LOGANDO)) {
					String login = msgCompleta.split(" ")[0];
					mapaDeCliente.put(login, bfw);
					Usuario usuario = new Usuario(login, msgCompleta.split(" ")[1]);
					usuario = daoUsuario.buscarLogin(usuario);
					if (usuario != null) {
						usuario.setLogado(true);
						daoUsuario.salvar(usuario);
						clientesEntraram.add(usuario.toString());

						msgCompleta = "";
						for (String cliente : clientesEntraram) {
							msgCompleta += " " + cliente;
						}

						sendLogin(bfw, msgCompleta + " " + SUCESSO + " " + ENTROU_NA_SALA);
						send(bfw, msgCompleta + " " + ENTROU_NA_SALA);
					}

				} else if (msgCompleta.contains(MSG_PRIVADA)) {
					String clienteDestinarario = msgCompleta.split("-")[1];

					for (String i : msgCompleta.split("-")) {
						System.out.println("split " + i);
					}
					System.out.println("Destino " + clienteDestinarario.trim());

					BufferedWriter bfwDestinario = mapaDeCliente.get(clienteDestinarario.trim());
					System.out.println("BFW" + bfwDestinario);
					sendPrivado(bfwDestinario, msgCompleta);

				} else if (msgCompleta.contains(REQUISITAR_PRIVADO)) {
					String clienteDestinarario = msgCompleta.split("-")[0];
					System.out.println(clienteDestinarario);
					BufferedWriter bfwDestinario = mapaDeCliente.get(clienteDestinarario.trim());
					System.out.println("BFW 2" + bfwDestinario);
					sendPrivado(bfwDestinario, msgCompleta);
				} else if (msgCompleta.contains(CASDATRAR)) {
					String login = msgCompleta.split("-")[0];
					String senha = msgCompleta.split("-")[1];

					Usuario usuario = new Usuario(login.trim(), senha.trim());

					if (!(daoUsuario.buscarLoginIgual(usuario) != null)) {
						daoUsuario.salvar(usuario);
						sendLogin(bfw, msgCompleta + LOGIN_ACEITO);
						Alerta alerta = Alerta.getInstace(null);
						alerta.alertar(AlertType.INFORMATION, "Atenção", "Sucesso", "Login cadastrado com sucesso.");
					} else {
						Alerta alerta = Alerta.getInstace(null);
						alerta.alertar(AlertType.INFORMATION, "Atenção", "Login existente",
								"Por favor escolha outro login.");

					}

				} else if (msgCompleta.contains(SAIR)) {
					String login = msgCompleta.split(" ")[0];
					mapaDeCliente.put(login, bfw);
					Usuario usuario = new Usuario(login, msgCompleta.split(" ")[1]);
					usuario = daoUsuario.buscarLogin(usuario);
					usuario.setLogado(false);
					daoUsuario.salvar(usuario);

				} else if (msgCompleta.contains(ULTIMO_ONLINE)) {
					String clienteDestinarario = msgCompleta.split("-")[0];
					System.out.println(clienteDestinarario);
					BufferedWriter bfwDestinario = mapaDeCliente.get(clienteDestinarario.trim());
					System.out.println("BFW 2" + bfwDestinario);
					sendPrivado(bfwDestinario, msgCompleta);

				} else if (msgCompleta.contains(RECUPERAR_MENSAGENS_OFFLINE)) {
					String clienteDestinarario = msgCompleta.split("-")[0];
					Usuario usuario = daoUsuario.buscarPeloLogin(clienteDestinarario.trim());
					System.out.println("Usuario " + usuario);
					System.out.println("Cliente Destino  " + clienteDestinarario.trim());
					
					for (MensagensDaSala mensagensDaSala : daoMsgDaSala.buscarMsgOffline(usuario)) {
						msgCompleta += " - " + mensagensDaSala.getLogin() + ": " + mensagensDaSala.getMsg() + "\n";
					}

					System.out.println("Mensagem completa " + msgCompleta);
					BufferedWriter bfwDestinario = mapaDeCliente.get(clienteDestinarario.trim());
					System.out.println("BFW 2" + bfwDestinario);

					sendLogin(bfwDestinario, msgCompleta);

				} else {
					send(bfw, msgCompleta);
				}

			}

		} catch (SocketException e) {
			Alerta alerta = Alerta.getInstace(Alert.AlertType.WARNING);
			alerta.alertar(Alert.AlertType.WARNING, "Atenção", "Alguém foi desconectado", "Atenção");

		} catch (IOException e) {
			Alerta alerta = Alerta.getInstace(Alert.AlertType.WARNING);
			alerta.alertar(AlertType.WARNING, "Atenção", "Erro ao carregar arquivo", "Atenção");
		}
	}

	public void sendPrivado(BufferedWriter bwSaida, String msg) {
		try {
			BufferedWriter bwS;
			for (BufferedWriter bw : clientes) {
				bwS = (BufferedWriter) bw;
				if (bwSaida == bwS) {
					// System.out.println(msg);
					bw.write(msg + "\r\n");
					bw.flush();
				}

			}
		} catch (IOException e) {
			Alerta alerta = Alerta.getInstace(AlertType.NONE);
			alerta.alertar(AlertType.WARNING, "Atenção", "Atenção", "Erro ao tentar carregar o arquivo!");
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

					if (!(msg.contains(DIGITANDO) || msg.contains(NAO_DIGITANDO) || msg.contains(ENTROU_NA_SALA))) {
						String login = msg.split(":")[0];
						String msgReal = msg.split(":")[1];

						for (Usuario usuario : daoUsuario.buscarUsuariosOff()) {
							MensagensDaSala mensagensDaSala = new MensagensDaSala(login.trim(), msgReal.trim());
							mensagensDaSala.setUsuarioOff(usuario);
							daoMsgDaSala.salvar(mensagensDaSala);

						}

					}

				}

			}
		} catch (IOException e) {
			Alerta alerta = Alerta.getInstace(AlertType.NONE);
			alerta.alertar(AlertType.WARNING, "Atenção", "Atenção", "Erro ao tentar carregar o arquivo!");
		}
	}

	public void sendLogin(BufferedWriter bwSaida, String msg) {
		try {

			System.out.println(msg);
			bwSaida.write(msg + "\r\n");
			bwSaida.flush();

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
