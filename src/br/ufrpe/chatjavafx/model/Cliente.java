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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class Cliente {
	private BufferedWriter bfw;
	public static final String DIGITANDO = "--digitando--";
	public static final String NAO_DIGITANDO = "--nao_digitando--";
	private static final String LOGANDO = "--LOGANDO--";
	private static final String SUCESSO = "--SUCESSO--";
	private static final String ENTROU_NA_SALA = "--ENTROU--";
	public static final String MSG_PRIVADA = "MSG_PRIVADA";
	public static final String REQUISITAR_PRIVADO = "REQUISITAR_PRIVADO";
	
	private Socket socket;
	private OutputStream ou;
	private Writer ouw;
	private BufferedReader bfr;
	private Task<Void> taskDigitando;
	private Thread threadDigitando;
	private String ip, nome, porta;
	private boolean logado;

	private ControllerCliente meuControllerCliente;

	@FXML
	private Label lbNome;

	@FXML
	private TextArea taTexto;

	@FXML
	private TextField tfMsg;

	@FXML
	private Label lbDigitando;

	@FXML
	private ListView<String> lvOlnine;

	public Cliente(Label lbNome, Label lbDigitando, TextArea taTexto, TextField tfmsg) {
		this.lbNome = lbNome;
		this.lbDigitando = lbDigitando;
		this.taTexto = taTexto;
		this.tfMsg = tfmsg;
	}

	public Cliente() {

	}

	public void conectar() {

		try {
			socket = new Socket(ip, 12345);
			ou = socket.getOutputStream();
			ouw = new OutputStreamWriter(ou);
			bfw = new BufferedWriter(ouw);
			bfw.write(nome + "\r\n");
			InputStream in = socket.getInputStream();
			InputStreamReader inr = new InputStreamReader(in);
			bfr = new BufferedReader(inr);
			// lbNome.setText(getNome());
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
							} else if (msg.contains(SUCESSO) && msg.contains(ENTROU_NA_SALA)) {
								msg = msg.replace(LOGANDO, "");
								msg = msg.replace(SUCESSO, "");
								msg = msg.replace(ENTROU_NA_SALA, "");
								msg = msg.replace("\n", "");

								ObservableList<String> ob = FXCollections.observableArrayList(msg.split(" "));
								lvOlnine.setItems(ob);

								logado = true;
							} else if (msg.contains(ENTROU_NA_SALA)) {
								System.out.println("Entrou");
								msg = msg.replace(LOGANDO, "");
								msg = msg.replace(SUCESSO, "");
								msg = msg.replace(ENTROU_NA_SALA, "");
								msg = msg.replace("\n", "");

								ObservableList<String> ob = FXCollections.observableArrayList(msg.split(" "));
								lvOlnine.setItems(ob);
							} else if (msg.contains(MSG_PRIVADA) && msg.contains(DIGITANDO)
									|| msg.contains(NAO_DIGITANDO)) {

							} else if (msg.contains(MSG_PRIVADA)) {
								msg = msg.replace(MSG_PRIVADA, "");
								msg = msg.replace(msg.split("-")[1], "");
								msg = msg.replaceAll("-", "");
								meuControllerCliente.getControllerPrivado1().getTaTexto().appendText(msg + "\r\n");
								
							} else if (msg.contains(REQUISITAR_PRIVADO)){
								String minhaMsg = msg;
								Task<Void> taskAtualizar = new Task<Void>() {
									@Override
									protected Void call() throws Exception {
										Platform.runLater(new Runnable() {

											@Override
											public void run() {

												try {

													System.out.println("Requisição" + minhaMsg);
													String nomeTab = minhaMsg.split("-")[2];
													Tab tab = new Tab(nomeTab);
													FXMLLoader loader = new FXMLLoader();
													loader.setLocation(getClass().getResource("/br/ufrpe/chatjavafx/view/Privado.fxml"));
													Parent root;
													root = loader.load();
													tab.setContent(root);
													meuControllerCliente.getTabPane().getTabs().add(tab);
													meuControllerCliente.getTabPane().getSelectionModel().select(tab);
													


													meuControllerCliente.setControllerPrivado1(loader.getController()) ;
													meuControllerCliente.getControllerPrivado1().setCliente(meuControllerCliente.getCliente());
													meuControllerCliente.getControllerPrivado1().setClienteDestino(nomeTab);
													meuControllerCliente.getControllerPrivado1().setNome(nome);
													
													//cliente.enviarMensagem(nome + "-" + " Privado:" + " - " + clienteTabela + " - "  + REQUISITAR_PRIVADO  );
													meuControllerCliente.getControllerPrivado1().getTaTexto().appendText(minhaMsg + "\r\n");
													
												} catch (IOException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												
											}

										});
										return null;

									}
								};

								Thread thread = new Thread(taskAtualizar);
								thread.setDaemon(true);
								thread.start();

							}else {
								taTexto.appendText(msg + "\r\n");
							}

						}

					}
				}
			}
		};
		Thread threadEscutar = new Thread(taskEscutar);
		threadEscutar.setDaemon(true);
		threadEscutar.setPriority(7);
		threadEscutar.start();

	}

	public void enviarMensagem(String msg) {
		try {
			if (msg.equals("Sair") || msg.equals("sair")) {
				bfw.write("Desconectado \r\n");
				taTexto.appendText("Desconectado \r\n");
			} else {

				if (msg.contains(DIGITANDO) || msg.contains(NAO_DIGITANDO) || msg.contains(LOGANDO) || msg.contains(REQUISITAR_PRIVADO) ) {
					bfw.write(msg + "\r\n");
				}else if(msg.contains(MSG_PRIVADA)) {
					bfw.write(msg + "\r\n");
					msg = msg.replace(MSG_PRIVADA, "");
					msg = msg.replace(msg.split("-")[1], "");
					msg = msg.replaceAll("-", "");
					meuControllerCliente.getControllerPrivado1().getTaTexto().appendText(msg + "\r\n");
					
				}else {
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
							lbDigitando.setText(situacao.split(" ")[1] + " está digitando");
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

	public Label getLbNome() {
		return lbNome;
	}

	public void setLbNome(Label lbNome) {
		this.lbNome = lbNome;
	}

	public TextArea getTaTexto() {
		return taTexto;
	}

	public void setTaTexto(TextArea taTexto) {
		this.taTexto = taTexto;
	}

	public TextField getTfMsg() {
		return tfMsg;
	}

	public void setTfMsg(TextField tfMsg) {
		this.tfMsg = tfMsg;
	}

	public Label getLbDigitando() {
		return lbDigitando;
	}

	public void setLbDigitando(Label lbDigitando) {
		this.lbDigitando = lbDigitando;
	}

	public boolean isLogado() {
		return logado;
	}

	public void setLogado(boolean logado) {
		this.logado = logado;
	}

	public ListView<String> getLvOlnine() {
		return lvOlnine;
	}

	public void setLvOlnine(ListView<String> lvOlnine) {
		this.lvOlnine = lvOlnine;
	}

	public ControllerCliente getMeuControllerCliente() {
		return meuControllerCliente;
	}

	public void setMeuControllerCliente(ControllerCliente meuControllerCliente) {
		this.meuControllerCliente = meuControllerCliente;
	}

}
