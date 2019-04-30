package br.ufrpe.chatjavafx.control;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import br.ufrpe.chatjavafx.model.Cliente;
import javafx.application.Application;
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
import javafx.stage.Stage;

public class ControllerDialogCliente extends Application implements Initializable {

	@FXML
	private JFXTextField tfNome;

	@FXML
	private JFXTextField tfIp;

	@FXML
	private JFXTextField tfPorta;

	@FXML
	private JFXButton btnConfirmar;

	private FXMLLoader loader;

	private static Stage meuStage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tfPorta.setText("12345");
		tfIp.setText("127.0.0.1");
	}

	@FXML
	void acaoBtnConfirmar(ActionEvent event) throws IOException {
		exibirTelaCliente();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/br/ufrpe/chatjavafx/view/DialogCliente.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		// primaryStage.initStyle(StageStyle.UNDECORATED);
		meuStage = primaryStage;
		primaryStage.show();

	}

	public void exibirTelaCliente() throws IOException {
		loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/br/ufrpe/chatjavafx/view/TelaCliente.fxml"));
		Parent root = loader.load();
		Stage stage = new Stage();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		ControllerCliente controllerCliente = loader.getController();
		System.out.println(controllerCliente);
		controllerCliente.setIp(tfIp.getText());
		controllerCliente.setNome(tfNome.getText());
		controllerCliente.setPorta(tfPorta.getText());
		controllerCliente.conectar();
		Cliente.controllerClientes.add(controllerCliente);

		ObservableList<ControllerCliente>  observableList = FXCollections.observableArrayList(Cliente.controllerClientes);
		
		for(ControllerCliente controllerCliente2: Cliente.controllerClientes) {
			controllerCliente2.getLvOlnine().setItems(observableList);
		}
		
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
		meuStage.setIconified(true);

		// primaryStage.initStyle(StageStyle.UNDECORATED);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public JFXTextField getTfNome() {
		return tfNome;
	}

	public JFXTextField getTfIp() {
		return tfIp;
	}

	public JFXTextField getTfPorta() {
		return tfPorta;
	}

	public JFXButton getBtnConfirmar() {
		return btnConfirmar;
	}

	public FXMLLoader getLoader() {
		return loader;
	}

	public Stage getMeuStage() {
		return meuStage;
	}
	

}
