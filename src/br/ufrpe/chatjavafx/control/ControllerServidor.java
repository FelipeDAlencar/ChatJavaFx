package br.ufrpe.chatjavafx.control;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import br.ufrpe.chatjavafx.jpa.ConnectionFactory;
import br.ufrpe.chatjavafx.model.Servidor;
import br.ufrpe.chatjavafx.model.Usuario;
import br.ufrpe.chatjavafx.model.dao.DAOUsuario;
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
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class ControllerServidor extends Application implements Initializable {
	@FXML
	private JFXTextField tfPorta;
	@FXML
	private JFXButton btnConfirmar;

	public static String porta = "";

	private Alerta alerta;
	private static Stage meuStage;
	private static ServerSocket server;
	private FXMLLoader loader;
	private DAOUsuario daoUsuario;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		daoUsuario = DAOUsuario.getInstance();
		tfPorta.setText("12345");

		tfPorta.setOnKeyPressed((evt) -> {
			if (evt.getCode() == KeyCode.ENTER) {
				estabelecerConexao();
			}
		});
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/br/ufrpe/chatjavafx/view/TelaServidor.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		meuStage = primaryStage;
		primaryStage.show();

	}

	public void estabelecerConexao() {
		try {
			tfPorta.setEditable(false);
			server = new ServerSocket(Integer.parseInt(tfPorta.getText()));
			alerta = Alerta.getInstace(Alert.AlertType.NONE);
			alerta.alertar(AlertType.INFORMATION, "Conexão", "Server concetado",
					"Server conectado a porta " + tfPorta.getText());
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

		} catch (NumberFormatException e) {
			Alerta alerta = Alerta.getInstace(AlertType.WARNING);
			alerta.alertar(AlertType.WARNING, "Atenção", "Antenção", "Informe um número válido!");
			tfPorta.setEditable(true);

		} catch (IOException e) {
			Alerta alerta = Alerta.getInstace(AlertType.WARNING);
			alerta.alertar(AlertType.WARNING, "Atenção", "Antenção", "Erro ao tentar carregar arquivo!");
		}

	}

	@FXML
	void acaoBtnConfirmar(ActionEvent event) {
		estabelecerConexao();
	}

	public static void main(String[] args) {
		launch(args);

	}

}
