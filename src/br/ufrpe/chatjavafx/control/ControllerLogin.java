package br.ufrpe.chatjavafx.control;

import java.awt.Dialog;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import br.ufrpe.chatjavafx.model.Usuario;
import br.ufrpe.chatjavafx.model.dao.DAOUsuario;
import br.ufrpe.chatjavafx.view.Alerta;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ControllerLogin extends Application implements Initializable {

	@FXML
	private JFXTextField tfLogin;

	@FXML
	private JFXPasswordField tfSenha;

	@FXML
	private JFXButton btnEntrar;

	private DAOUsuario daoUsuario;
	
	private Usuario usuario;
	
	private static Stage meuStage;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		daoUsuario = DAOUsuario.getInstance();
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
	void AcaoBtnEntrar(ActionEvent event) {

		usuario = new Usuario();
		usuario.setLogin(tfLogin.getText());
		usuario.setSenha(tfSenha.getText());
		usuario = daoUsuario.buscarLogin(usuario);
		if (usuario != null) {
			usuario.setLogado(true);
			daoUsuario.salvar(usuario);
			meuStage.setIconified(true);
			exibirDialogCliente();
		}

	}

	public void exibirDialogCliente() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/br/ufrpe/chatjavafx/view/DialogCliente.fxml"));
			Parent root;
			root = loader.load();
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			ControllerDialogCliente.meuStage = stage;
			stage.setOnCloseRequest((event) -> {
				usuario.setLogado(false);
				daoUsuario.salvar(usuario);
				System.exit(0);
			});
			
			stage.show();
			ControllerDialogCliente.usuario = usuario;
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}

}
