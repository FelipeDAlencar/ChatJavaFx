package br.ufrpe.chatjavafx.control;

import java.awt.event.KeyEvent;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import br.ufrpe.chatjavafx.model.Cliente;
import br.ufrpe.chatjavafx.model.Servidor;
import javafx.application.Application;
import javafx.application.Platform;
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

	@FXML
	private JFXButton btnPrivado;

	@FXML
	private ListView<ControllerCliente> lvOlnine;

	@FXML
	private Label lbNome;

	private static Stage meuStage = null;

	private Cliente cliente;
	private String nome, ip, porta;
	private ControllerCliente controllerClientePrivado;
	private static FXMLLoader loader;

	public static ArrayList<ControllerCliente> controllerClientes = new ArrayList<>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cliente = new Cliente(lbNome, lbDigitando, taTexto, tfMsg);
		lvOlnine.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> selecionouDoTv(newValue));

		tfMsg.setOnKeyPressed((evt) -> {
			if (evt.getCode() == KeyCode.ENTER) {

				cliente.enviarMensagem(nome + ": " + tfMsg.getText());

			} else {

				cliente.enviarMensagem(Cliente.DIGITANDO);

			}
		});

		tfMsg.setOnKeyReleased((evt) -> {

			cliente.enviarMensagem(Cliente.NAO_DIGITANDO);

		});

	}

	private void selecionouDoTv(ControllerCliente controllerCliente) {
		if (controllerCliente != null) {
			controllerClientePrivado = controllerCliente;
		}
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

	}

	@FXML
	void acaoBtn(ActionEvent event) {
		if (event.getSource() == btnSair) {
			meuStage.close();
		} else if (event.getSource() == btnPrivado) {
			exibirTelaCliente();

		} else {
			cliente.enviarMensagem(nome + ": " + tfMsg.getText());
		}
	}

	public void exibirTelaCliente() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/br/ufrpe/chatjavafx/view/TelaCliente.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);

			ControllerCliente meuControllerCliente = loader.getController();
			meuControllerCliente.getLbNome().setText(nome);

			FXMLLoader loader2 = new FXMLLoader();
			loader2.setLocation(getClass().getResource("/br/ufrpe/chatjavafx/view/TelaCliente.fxml"));
			Parent root2 = loader2.load();
			Stage stage2 = new Stage();
			Scene scene2 = new Scene(root2);
			stage2.setScene(scene2);

			loader2.setController(controllerClientePrivado);
			controllerClientePrivado.getLbNome().setText(controllerClientePrivado.getNome());
			System.out.println(cliente.getBfw());

			cliente.conectar();
			lbNome.setText(nome);

			Task<Void> taskEscutar = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					cliente.escutar();
					cliente.escutar();
					return null;
				}
			};
			Thread threadEscutar = new Thread(taskEscutar);
			threadEscutar.setDaemon(true);
			threadEscutar.start();

			stage.show();
			stage2.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sair() throws IOException {

		cliente.enviarMensagem("Sair");
		cliente.getBfw().close();
		cliente.getOuw().close();
		cliente.getOu().close();
		cliente.getSocket().close();
	}



	@Override
	public String toString() {
		return nome;
	}

	public static void main(String[] args) {
		launch(args);

	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPorta() {
		return porta;
	}

	public void setPorta(String porta) {
		this.porta = porta;
	}

	public ListView<ControllerCliente> getLvOlnine() {
		return lvOlnine;
	}

	public Label getLbNome() {
		return lbNome;
	}

	public Cliente getCliente() {
		return cliente;
	}
	
	
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}
