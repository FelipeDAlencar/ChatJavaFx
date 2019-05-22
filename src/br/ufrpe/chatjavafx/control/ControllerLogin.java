package br.ufrpe.chatjavafx.control;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import br.ufrpe.chatjavafx.model.Cliente;
import br.ufrpe.chatjavafx.model.Usuario;
import br.ufrpe.chatjavafx.view.Alerta;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ControllerLogin extends Application implements Initializable {

	private static final String LOGANDO = "--LOGANDO--";
	public static final String SAIR = "--SAIR--";

	@FXML
	private JFXTextField tfIp;

	@FXML
	private JFXTextField tfLogin;

	@FXML
	private JFXPasswordField tfSenha;

	@FXML
	private JFXButton btnEntrar;

	@FXML
	private Label lbCadastrar;

	private Cliente cliente;

	private Stage meuStage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tfIp.setText("127.0.0.1");
		cliente = new Cliente();

		lbCadastrar.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				cliente.setIp(tfIp.getText());
				cliente.conectar();

				exibirTelaCadastro();

			}
		});

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/br/ufrpe/chatjavafx/view/Login.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);

		meuStage = primaryStage;
		primaryStage.show();

	}

	@FXML
	void AcaoBtnEntrar(ActionEvent event) throws InterruptedException {

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/br/ufrpe/chatjavafx/view/TelaCliente.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);

			ControllerCliente controllerCliente = loader.getController();
			controllerCliente.setCliente(cliente);
			controllerCliente.setIp(tfIp.getText());

			controllerCliente.setNome(tfLogin.getText());
			controllerCliente.setPorta("12345");

			controllerCliente.getCliente().setIp(tfIp.getText());
			controllerCliente.getCliente().setPorta("12345");
			controllerCliente.getCliente().setLogin(tfLogin.getText());
			controllerCliente.getCliente().getLbNome().setText(tfLogin.getText());

			cliente.setIp(tfIp.getText());
			cliente.conectar();

			cliente.enviarMensagem(tfLogin.getText() + " " + tfSenha.getText() + " " + LOGANDO);

			controllerCliente.setMeuStage(stage);

			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent event) {
					cliente.enviarMensagem(tfLogin.getText() + " " + tfSenha.getText() + " " + SAIR);
				}
			});

			meuStage.setIconified(true);

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("cliente" + cliente.isLogado());

	}

	public void exibirTelaCadastro() {

		try {

			meuStage.setIconified(true);

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/br/ufrpe/chatjavafx/view/Cadastro.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);

			ControllerCadastro controllerCadastro = loader.getController();
			controllerCadastro.setCliente(cliente);

			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}

	public Stage getMeuStage() {
		return meuStage;
	}

	public void setMeuStage(Stage meuStage) {
		this.meuStage = meuStage;
	}

}
