package br.ufrpe.chatjavafx.control;

import java.awt.Event;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import br.ufrpe.chatjavafx.view.Alerta;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ControllerDialogCliente extends Application implements Initializable {
	@FXML
	private VBox vbox;

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

	@FXML
	private static Scene scene;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		tfPorta.setText("12345");
		tfIp.setText("127.0.0.1");

		tfNome.setOnKeyPressed((evt) -> {
			if (evt.getCode() == KeyCode.ENTER) {
				exibirTelaCliente();
			}
		});
		tfIp.setOnKeyPressed((evt) -> {
			if (evt.getCode() == KeyCode.ENTER) {
				exibirTelaCliente();
			}
		});
		
		tfPorta.setOnKeyPressed((evt) -> {
			if (evt.getCode() == KeyCode.ENTER) {
				exibirTelaCliente();
			}
		});
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
		scene = new Scene(root);
		primaryStage.setScene(scene);
		meuStage = primaryStage;

		primaryStage.show();

	}

	public void exibirTelaCliente() {

		try {
			loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/br/ufrpe/chatjavafx/view/TelaCliente.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			ControllerCliente controllerCliente = loader.getController();

			controllerCliente.setIp(tfIp.getText());

			controllerCliente.setNome(tfNome.getText());
			controllerCliente.setPorta(tfPorta.getText());

			controllerCliente.getCliente().setIp(tfIp.getText());
			controllerCliente.getCliente().setPorta(tfPorta.getText());
			controllerCliente.getCliente().setNome(tfNome.getText());
			System.out.println(controllerCliente.getCliente().getNome());
			controllerCliente.getCliente().conectar();
			
			
			ControllerCliente.controllerClientes.add(controllerCliente);
			ObservableList<ControllerCliente> observableList = FXCollections
					.observableArrayList(ControllerCliente.controllerClientes);
			for (ControllerCliente controllerCliente2 : ControllerCliente.controllerClientes) {
				controllerCliente2.getLvOlnine().setItems(observableList);
			}

			meuStage.setIconified(true);

			// primaryStage.initStyle(StageStyle.UNDECORATED);
			stage.show();
		} catch (Exception e) {
			Alerta alerta = Alerta.getInstace(Alert.AlertType.NONE);
			alerta.alertar(AlertType.WARNING, "Atenção", "Erro ao carregar arquivo", "Atenção");
			e.printStackTrace();
		}
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
