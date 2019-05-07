package br.ufrpe.chatjavafx.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;

import br.ufrpe.chatjavafx.control.ControllerCliente;
import br.ufrpe.chatjavafx.view.Alerta;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class Cliente {
	private BufferedWriter bfw;
	public static final String DIGITANDO = "--digitando--";
	public static final String NAO_DIGITANDO = "--nao_digitando--";
	private Socket socket;
	private OutputStream ou;
	private Writer ouw;
	private BufferedReader bfr;
	private Task<Void> taskDigitando;
	private Thread threadDigitando;
	private String ip, nome, porta;
	@FXML
	private Label lbNome;

	@FXML
	private TextArea taTexto;

	@FXML
	private TextField tfMsg;

	@FXML
	private Label lbDigitando;

	public Cliente(Label lbNome, Label lbDigitando, TextArea taTexto, TextField tfmsg) {
		this.lbNome = lbNome;
		this.lbDigitando = lbDigitando;
		this.taTexto = taTexto;
		this.tfMsg = tfmsg;
	}

	public Cliente(Label lbNome, Label lbDigitando, TextArea taTexto, TextField tfmsg, Socket socket) {
		this.socket = socket;
		this.lbNome = lbNome;
		this.lbDigitando = lbDigitando;
		this.taTexto = taTexto;
		this.tfMsg = tfmsg;
	}

	public void conectar() {

		try {
			socket = new Socket(ip, Integer.parseInt(porta));
			ou = socket.getOutputStream();
			ouw = new OutputStreamWriter(ou);
			bfw = new BufferedWriter(ouw);
			Servidor.clientes.add(bfw);
			bfw.write(nome + "\r\n");
			InputStream in = socket.getInputStream();
			InputStreamReader inr = new InputStreamReader(in);
			bfr = new BufferedReader(inr);
			lbNome.setText(getNome());
			System.out.println(getNome());
			bfw.flush();

			escutar();
			
			System.out.println("Conectou");
		} catch (NumberFormatException e) {
			Alerta alerta = Alerta.getInstace(AlertType.WARNING);
			alerta.alertar(AlertType.WARNING, "Atenção", "Antenção", "Informe um número inválido!");

		} catch (IOException e) {
			Alerta alerta = Alerta.getInstace(AlertType.WARNING);
			alerta.alertar(AlertType.WARNING, "Atenção", "Antenção", "Erro ao tentar carregar arquivo!");
		}

	}

	public void enviarMensagem(String msg) {
		try {
			if (msg.equals("Sair") || msg.equals("sair")) {
				bfw.write("Desconectado \r\n");
				taTexto.appendText("Desconectado \r\n");
			} else {

				if (msg.contains(DIGITANDO) || msg.contains(NAO_DIGITANDO)) {
					bfw.write(msg + "\r\n");
				} else {
					bfw.write(msg + "\r\n");
					taTexto.appendText(getNome() + ": " + tfMsg.getText() + "\r\n");
					tfMsg.setText("");
				}

				System.out.println(msg);

			}
			bfw.flush();

		} catch (IOException e) {
			Alerta alerta = Alerta.getInstace(AlertType.NONE);
			alerta.alertar(AlertType.WARNING, "Atenção", "Atenção", "Erro ao tentar carregar o arquivo!");
		}
	}

	public void atualizarDigitando(String situacao) {
		Task<Void> taskAtualizar = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {

						if (situacao.contains(Cliente.DIGITANDO)) {
							System.out.println("Metodo" + situacao);
							lbDigitando.setText(situacao.split(" ")[1] +  " está digitando");
						} else {
							lbDigitando.setText("");
						}
					}

				});
				return null;

			}
		};

		Thread thread = new Thread(taskAtualizar);
		thread.setDaemon(true);
		thread.start();

		taskAtualizar = null;
		System.gc();
	}

	public void escutar() throws IOException {

		Task<Void> taskEscutar = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				String msg = "";
				while (true) {
					if (bfr.ready()) {
						msg = bfr.readLine();
						if (msg.equals("Sair") || msg.equals("sair")) {
							taTexto.appendText("Servidor caiu! \r\n");
						} else {
							if (msg.contains(DIGITANDO)) {
								atualizarDigitando(msg);
							} else if (msg.contains(NAO_DIGITANDO)) {
								atualizarDigitando(NAO_DIGITANDO);
							} else {
								taTexto.appendText(msg + "\r\n");
							}

						}

					}
				}
			}
		};
		Thread threadEscutar = new Thread(taskEscutar);
		threadEscutar.setDaemon(true);
		threadEscutar.start();

	}

	public BufferedWriter getBfw() {
		return bfw;
	}

	public Socket getSocket() {
		return socket;
	}

	public OutputStream getOu() {
		return ou;
	}

	public Writer getOuw() {
		return ouw;
	}

	public BufferedReader getBfr() {
		return bfr;
	}

	public Task<Void> getTaskDigitando() {
		return taskDigitando;
	}

	public Thread getThreadDigitando() {
		return threadDigitando;
	}

	public String getPorta() {
		return porta;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public void setOu(OutputStream ou) {
		this.ou = ou;
	}

	public void setOuw(Writer ouw) {
		this.ouw = ouw;
	}

	public void setPorta(String porta) {
		this.porta = porta;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
