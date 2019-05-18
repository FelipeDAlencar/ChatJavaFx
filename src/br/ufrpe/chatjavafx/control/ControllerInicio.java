package br.ufrpe.chatjavafx.control;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ControllerInicio extends Application implements Initializable {
	
	
	@FXML
	private JFXButton btnServidor;

	@FXML
	private JFXButton btnCliente;
	
	@FXML
	private static Stage meuStage;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/br/ufrpe/chatjavafx/view/Inicial.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		
		meuStage = stage;
		
		stage.show();
		
	}
	@FXML
	void acaoBtns(ActionEvent event) {
		if(event.getSource() == btnCliente) {
			exibirTelaLogin("Login.fxml");
		}else {
			exibirTelaServidor("TelaServidor.fxml");
		}
		
		meuStage.close();
	}
	
	public void exibirTelaLogin(String caminho) {
	
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/br/ufrpe/chatjavafx/view/" + caminho));
			Parent root;
			root = loader.load();
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			
			ControllerLogin controllerLogin = loader.getController();
			controllerLogin.setMeuStage(stage);
			
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void exibirTelaServidor(String caminho) {
		
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/br/ufrpe/chatjavafx/view/" + caminho));
			Parent root;
			root = loader.load();
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			
			ControllerServidor controllerServidor = loader.getController();
			controllerServidor.setMeuStage(stage);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		launch(args);
	}

}
