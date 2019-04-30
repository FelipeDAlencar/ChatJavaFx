package br.ufrpe.chatjavafx.control;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import br.ufrpe.chatjavafx.model.Cliente;
import br.ufrpe.chatjavafx.model.Servidor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ControllerCliente extends Application implements Initializable {
	@FXML
	private Label lbDigitando;

	@FXML
	private JFXTextArea taTexto;

	@FXML
	private JFXTextField tfMsg;

	@FXML
	private JFXButton btnSair;

	@FXML
	private JFXButton btnPrivado;

	@FXML
	private ListView<ControllerCliente> lvOlnine;

	@FXML
	private Label lbNome;

	private static Stage meuStage = null;

	private BufferedWriter bfw;
	public static final String DIGITANDO = "--digitando--";
	public static final String NAO_DIGITANDO = "--nao_digitando--";
	private Socket socket;
	private OutputStream ou;
	private Writer ouw;
	private BufferedReader bfr;
	private static FXMLLoader loader;
	private Task<Void> taskDigitando;
	private Thread threadDigitando;
	private String nome, ip, porta;
	private ControllerCliente controllerClientePrivado;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		lvOlnine.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> selecionouDoTv(newValue));

		tfMsg.setOnKeyPressed((evt) -> {
			if (evt.getCode() == KeyCode.ENTER) {

				enviarMensagem(nome + ": " + tfMsg.getText());

			} else {

				enviarMensagem(DIGITANDO);

			}
		});

		tfMsg.setOnKeyReleased((evt) -> {

			enviarMensagem(NAO_DIGITANDO);

		});

	}

	private void selecionouDoTv(ControllerCliente controllerCliente) {
		if (controllerCliente != null) {
			controllerClientePrivado = controllerCliente;
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/br/ufrpe/chatjavafx/view/TelaCliente.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		// primaryStage.initStyle(StageStyle.UNDECORATED);
		meuStage = primaryStage;
		primaryStage.show();

	}

	@FXML
	void acaoBtn(ActionEvent event) {
		if (event.getSource() == btnSair) {
			meuStage.close();
		} else if (event.getSource() == btnPrivado) {
			exibirTelaCliente();

		} else {
			enviarMensagem(nome + ": " + tfMsg.getText());
		}
	}

	public void exibirTelaCliente() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/br/ufrpe/chatjavafx/view/TelaCliente.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);

			FXMLLoader loader2 = new FXMLLoader();
			loader2.setLocation(getClass().getResource("/br/ufrpe/chatjavafx/view/TelaCliente.fxml"));
			Parent root2 = loader2.load();
			Stage stage2 = new Stage();
			Scene scene2 = new Scene(root2);
			stage2.setScene(scene2);

			loader2.setController(controllerClientePrivado);

			controllerClientePrivado.conectar();

			Task<Void> taskEscutar = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					controllerClientePrivado.escutar();
					escutar();
					return null;
				}
			};
			Thread threadEscutar = new Thread(taskEscutar);
			threadEscutar.setDaemon(true);
			threadEscutar.start();
			meuStage.setIconified(true);

			// primaryStage.initStyle(StageStyle.UNDECORATED);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void conectar() throws IOException {

		socket = new Socket(ip, Integer.parseInt(porta));
		ou = socket.getOutputStream();
		ouw = new OutputStreamWriter(ou);
		bfw = new BufferedWriter(ouw);
		Servidor.clientes.add(bfw);
		lbNome.setText(nome);
		bfw.write(nome + "\r\n");
		InputStream in = socket.getInputStream();
		InputStreamReader inr = new InputStreamReader(in);
		bfr = new BufferedReader(inr);

		bfw.flush();
	}

	public void atualizarDigitando(String situacao) {
		Task<Void> taskAtualizar = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {

						if (situacao.equals(DIGITANDO)) {
							lbDigitando.setText(nome + " está digitando");
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

	public void enviarMensagem(String msg) {
		try {
			if (msg.equals("Sair") || msg.equals("sair")) {
				bfw.write("Desconectado \r\n");
				taTexto.appendText("Desconectado \r\n");
			} else {

				if (msg.equals(DIGITANDO) || msg.equals(NAO_DIGITANDO)) {
					bfw.write(msg + "\r\n");
				} else {
					bfw.write(msg + "\r\n");
					taTexto.appendText(nome + ": " + tfMsg.getText() + "\r\n");
					tfMsg.setText("");
				}

				System.out.println(msg);

			}
			bfw.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void escutar() throws IOException {

		String msg = "";
		while (true) {
			if (bfr.ready()) {
				msg = bfr.readLine();
				if (msg.equals("Sair") || msg.equals("sair")) {
					taTexto.appendText("Servidor caiu! \r\n");
				} else {
					if (msg.contains(DIGITANDO)) {
						atualizarDigitando(DIGITANDO);
					} else if (msg.contains(NAO_DIGITANDO)) {
						atualizarDigitando(NAO_DIGITANDO);
					} else {
						taTexto.appendText(msg + "\r\n");
					}

				}

			}
		}

	}

	public void sair() throws IOException {

		enviarMensagem("Sair");
		bfw.close();
		ouw.close();
		ou.close();
		socket.close();
	}

	@Override
	public String toString() {
		return nome;
	}

	public static void main(String[] args) {
		launch(args);

	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPorta() {
		return porta;
	}

	public void setPorta(String porta) {
		this.porta = porta;
	}

	public ListView<ControllerCliente> getLvOlnine() {
		return lvOlnine;
	}

	public BufferedWriter getBfw() {
		return bfw;
	}

	public BufferedReader getBfr() {
		return bfr;
	}

}
