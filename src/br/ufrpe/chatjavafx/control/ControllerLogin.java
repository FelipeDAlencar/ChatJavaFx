package br.ufrpe.chatjavafx.control;

import java.awt.Dialog;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import br.ufrpe.chatjavafx.model.Cliente;
import br.ufrpe.chatjavafx.model.Servidor;
import br.ufrpe.chatjavafx.model.Usuario;
import br.ufrpe.chatjavafx.model.dao.DAOUsuario;
import br.ufrpe.chatjavafx.view.Alerta;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ControllerLogin extends Application implements Initializable {
	
	private static final String LOGANDO = "--LOGANDO--";
	@FXML
	private JFXTextField tfIp;

	@FXML
	private JFXTextField tfLogin;

	@FXML
	private JFXPasswordField tfSenha;

	@FXML
	private JFXButton btnEntrar;


	private Usuario usuario;
	private Cliente cliente;
	
	private static Stage meuStage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tfIp.setText("127.0.0.1");
		cliente =  new Cliente();
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
		System.out.println(cliente);
		cliente.setIp(tfIp.getText());
		cliente.conectar();
		
		cliente.enviarMensagem(tfLogin.getText() + " "+ tfSenha.getText() + " " +LOGANDO);
		
		Thread.sleep(2000);
		if(cliente.isLogado()) {
			exibirTelaCliente();
		}else {
			Alerta alerta =  Alerta.getInstace(null);
			alerta.alertar(AlertType.INFORMATION, "Problema ao tentar logar", "Problema ao tentar logar", "Verifique o seu login e senha");
		}
		
		System.out.println("cliente" + cliente.isLogado());

	}

	public void exibirTelaCliente() {

		try {

			meuStage.setIconified(true);

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
			controllerCliente.getCliente().setNome(tfLogin.getText());
			controllerCliente.getCliente().getLbNome().setText(tfLogin.getText());
			//controllerCliente.getCliente().conectar();
			
			Servidor.clientes.add(controllerCliente.getCliente().getBfw());
			ControllerCliente.controllerClientes.add(controllerCliente);

	
			// primaryStage.initStyle(StageStyle.UNDECORATED);
			stage.show();
		} catch (Exception e) {
			Alerta alerta = Alerta.getInstace(Alert.AlertType.NONE);
			alerta.alertar(AlertType.WARNING, "Atenção", "Erro ao carregar arquivo", "Atenção");
			e.printStackTrace();
		}

		// try {
		// FXMLLoader loader = new FXMLLoader();
		// loader.setLocation(getClass().getResource("/br/ufrpe/chatjavafx/view/DialogCliente.fxml"));
		// Parent root;
		// root = loader.load();
		// Scene scene = new Scene(root);
		// Stage stage = new Stage();
		// stage.setScene(scene);
		// ControllerDialogCliente.meuStage = stage;
		// stage.setOnCloseRequest((event) -> {
		// usuario.setLogado(false);
		// daoUsuario.salvar(usuario);
		// System.exit(0);
		// });
		//
		// stage.show();
		// ControllerDialogCliente.usuario = usuario;
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

	}

	public static void main(String[] args) {
		launch(args);
	}

}
