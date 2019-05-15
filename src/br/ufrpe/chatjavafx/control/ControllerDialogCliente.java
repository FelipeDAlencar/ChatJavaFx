package br.ufrpe.chatjavafx.control;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import br.ufrpe.chatjavafx.model.ComunicacaoPrivada;
import br.ufrpe.chatjavafx.model.Servidor;
import br.ufrpe.chatjavafx.model.Usuario;
import br.ufrpe.chatjavafx.model.dao.DAOUsuario;
import br.ufrpe.chatjavafx.view.Alerta;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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

	public static Stage meuStage;

	@FXML
	private static Scene scene;

	ServerSocket serverSocket;

	public static Usuario usuario;

	private DAOUsuario daoUsuario;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		daoUsuario = DAOUsuario.getInstance();
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

		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {

					while (true) {
						Socket socket = serverSocket.accept();
						System.out.println("conectou");
						Thread thread = new ComunicacaoPrivada(socket, serverSocket);
						thread.setDaemon(true);
						thread.start();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		};
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
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
			
			meuStage.setIconified(true);
			
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

			controllerCliente.getCliente().conectar();
			Servidor.clientes.add(controllerCliente.getCliente().getBfw());
			ControllerCliente.controllerClientes.add(controllerCliente);

			DAOUsuario daoUsuario = DAOUsuario.getInstance();

			ObservableList<Usuario> observableList = FXCollections.observableArrayList(daoUsuario.buscarLogados());

//			for (ControllerCliente controllerCliente2 : ControllerCliente.controllerClientes) {
//				controllerCliente2.getLvOlnine().setItems(observableList);
//			}
//
//			Random random = new Random();
//			int portaPrivada = random.nextInt(12345);
//			serverSocket = new ServerSocket(portaPrivada);
//			controllerCliente.setPortaPrivada(portaPrivada);

			

			// primaryStage.initStyle(StageStyle.UNDECORATED);
			stage.show();
		} catch (Exception e) {
			Alerta alerta = Alerta.getInstace(Alert.AlertType.NONE);
			alerta.alertar(AlertType.WARNING, "Atenção", "Erro ao carregar arquivo", "Atenção");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println("Aqui");
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
