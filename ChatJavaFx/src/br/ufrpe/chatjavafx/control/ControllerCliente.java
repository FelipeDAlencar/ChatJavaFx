package br.ufrpe.chatjavafx.control;

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

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;	
import javafx.scene.Scene;
import javafx.scene.control.Label;
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

	private static Stage meuStage = null;

	private BufferedWriter bfw;
	// private JTextField txtIP;
	// private JTextField txtPorta;
	// private JTextField txtNome;
	// private JLabel lbDigitando;
	public static final String DIGITANDO = "--digitando--";
	public static final String NAO_DIGITANDO = "--nao_digitando--";
	private Socket socket;
	private OutputStream ou;
	private Writer ouw;
	private static FXMLLoader loader;
	private Task<Void> taskDigitando;
	private Thread threadDigitando;

	public void conectar() throws IOException {

		// socket = new Socket(txtIP.getText(), Integer.parseInt(txtPorta.getText()));
		socket = new Socket("127.0.0.1", 12345);
		ou = socket.getOutputStream();
		ouw = new OutputStreamWriter(ou);
		bfw = new BufferedWriter(ouw);
		bfw.write("Felipe" + "\r\n");
		bfw.flush();
	}
	

	public void atualizarDigitando(String situacao) {

		taskDigitando = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				// Demais códigos...
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if (situacao.equals(DIGITANDO)) {
							lbDigitando.setText("Felipe" + " está digitando");
						} else {
							lbDigitando.setText("");
						}
					}
				});
				return null;

			}
		};

		threadDigitando = new Thread(taskDigitando);
		threadDigitando.setDaemon(true);
		threadDigitando.start();
		
		taskDigitando = null;
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
					taTexto.appendText("Felipe" + ": " + tfMsg.getText() + "\r\n");
					tfMsg.setText("");
				}

				System.out.println(msg);
				bfw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void escutar() throws IOException {

		InputStream in = socket.getInputStream();
		InputStreamReader inr = new InputStreamReader(in);
		BufferedReader bfr = new BufferedReader(inr);
		String msg = "";

		while (true)

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

	public void sair() throws IOException {

		enviarMensagem("Sair");
		bfw.close();
		ouw.close();
		ou.close();
		socket.close();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		tfMsg.setOnKeyPressed((evt) -> {
			if (evt.getCode() == KeyCode.ENTER) {

				enviarMensagem("Felipe" + ": " + tfMsg.getText());

			} else {

				enviarMensagem(DIGITANDO);

			}
		});

		tfMsg.setOnKeyReleased((evt) -> {

			enviarMensagem(NAO_DIGITANDO);

		});

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
		ControllerCliente controllerCliente = loader.getController();
		try {
			controllerCliente.conectar();
			Task<Void> taskEscutar = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					controllerCliente.escutar();
					return null;
				}
			};
			Thread threadEscutar = new Thread(taskEscutar);
			threadEscutar.setDaemon(true);
			threadEscutar.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	void acaoBtn(ActionEvent event) {
		if (event.getSource() == btnSair) {
			meuStage.close();
		} else {
			enviarMensagem("Felipe" + ": " + tfMsg.getText());
		}
	}

	public static void main(String[] args) {
		launch(args);

	}

}
