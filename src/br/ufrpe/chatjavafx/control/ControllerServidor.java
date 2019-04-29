package br.ufrpe.chatjavafx.control;

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
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import br.ufrpe.chatjavafx.model.Servidor;
import br.ufrpe.chatjavafx.view.Alerta;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ControllerServidor extends Application implements Initializable {
	@FXML
	private JFXTextField tfPorta;
	@FXML
	private JFXButton btnConfirmar;

	public static String porta = "";

	private Alerta alerta;
	private static Stage meuStage;
	private static ArrayList<BufferedWriter> clientes;
	private static ServerSocket server;
	private String nome;
	private Socket con;
	private InputStream in;
	private InputStreamReader inr;
	private BufferedReader bfr;
	private FXMLLoader loader;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("Init");
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/br/ufrpe/chatjavafx/view/TelaServidor.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		// primaryStage.initStyle(StageStyle.UNDECORATED);
		meuStage = primaryStage;
		primaryStage.show();

	}

	@FXML
	void acaoBtnConfirmar(ActionEvent event) {
		try {
			tfPorta.setEditable(false);
			server = new ServerSocket(Integer.parseInt(tfPorta.getText()));
			alerta = Alerta.getInstace();
			alerta.alertar(AlertType.INFORMATION, "Conexão" , "Server concetado", "Server conectado a porta " + tfPorta.getText());
			meuStage.setIconified(true);
			Task<Void> teste = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					while (true) {
						System.out.println("Aguardando conexão...");
						Socket con = server.accept();
						System.out.println("Cliente conectado...");
						Thread t = new Servidor(con, server);
						t.setDaemon(true);
						t.start();
						
					}
				}

			};
			Thread t = new Thread(teste);
			t.setDaemon(true);
			t.start();

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);

	}

}
