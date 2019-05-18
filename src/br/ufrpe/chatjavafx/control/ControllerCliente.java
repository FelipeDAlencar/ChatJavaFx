package br.ufrpe.chatjavafx.control;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import javax.sound.midi.ControllerEventListener;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import br.ufrpe.chatjavafx.model.Cliente;
import br.ufrpe.chatjavafx.model.ComunicacaoPrivada;
import br.ufrpe.chatjavafx.model.Servidor;
import br.ufrpe.chatjavafx.model.Usuario;
import br.ufrpe.chatjavafx.model.dao.DAOUsuario;
import br.ufrpe.chatjavafx.view.Alerta;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class ControllerCliente extends Application implements Initializable {
	public static final String MSG_PRIVADA = "MSG_PRIVADA";
	public static final String REQUISITAR_PRIVADO = "REQUISITAR_PRIVADO";
	
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
	private ListView<String> lvOlnine;

	@FXML
	private Label lbNome;

	@FXML
	private Stage meuStage;

	@FXML
	private JFXTabPane tabPane;

	private Cliente cliente;
	private String nome, ip, porta;
	private static FXMLLoader loader;
	public static ArrayList<ControllerCliente> controllerClientes = new ArrayList<>();
	private ControllerPrivado controllerPrivado1;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cliente = new Cliente(lbNome, lbDigitando, taTexto, tfMsg);
	

		lvOlnine.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> selecionouDoTv(newValue));

		tfMsg.setOnKeyPressed((evt) -> {
			if (evt.getCode() == KeyCode.ENTER) {

				cliente.enviarMensagem(nome + ": " + tfMsg.getText());

			} else {

				cliente.enviarMensagem(Cliente.DIGITANDO + " " + nome);

			}
		});

		tfMsg.setOnKeyReleased((evt) -> {

			cliente.enviarMensagem(Cliente.NAO_DIGITANDO);

		});
		
		

	}

	private void selecionouDoTv(String clienteTabela) {
		if (clienteTabela != null) {
			try {
				Tab tab = new Tab(clienteTabela);
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/br/ufrpe/chatjavafx/view/Privado.fxml"));
				Parent root = loader.load();
				tab.setContent(root);
				tabPane.getTabs().addAll(tab);
				tabPane.getSelectionModel().select(tab);
				


				controllerPrivado1 = loader.getController();
				controllerPrivado1.setCliente(cliente);
				clienteTabela = clienteTabela.replace("+", "");
				clienteTabela = clienteTabela.replace("-", "");
				controllerPrivado1.setClienteDestino(clienteTabela);
				controllerPrivado1.setNome(nome);
				
				
				cliente.enviarMensagem(clienteTabela + " -  " + " Privado:" + " - " + nome + " - "  + REQUISITAR_PRIVADO  );
				

			} catch (IOException e) {
				Alerta alerta = Alerta.getInstace(null);
				alerta.alertar(AlertType.WARNING, "Atenção", "Atenção", "Erro ao tentar carregar arquivo!");
				e.printStackTrace();
			}

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
		meuStage.show();

	}

	@FXML
	void acaoBtn(ActionEvent event) {
		if (event.getSource() == btnSair) {
			System.exit(0);
		} else if (event.getSource() == btnPrivado) {
			novaTab();

		} else {
			cliente.enviarMensagem(nome + ": " + tfMsg.getText());
		}
	}

	public void novaTab() {
		
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

	public ListView<String> getLvOlnine() {
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
		cliente.setLbNome(lbNome);
		cliente.setTaTexto(taTexto);
		cliente.setLbDigitando(lbDigitando);
		cliente.setTfMsg(tfMsg);
		cliente.setLvOlnine(lvOlnine);
		cliente.setMeuControllerCliente(this);
	}

	public JFXTabPane getTabPane() {
		return tabPane;
	}


	public ControllerPrivado getControllerPrivado1() {
		return controllerPrivado1;
	}

	public void setControllerPrivado1(ControllerPrivado controllerPrivado1) {
		this.controllerPrivado1 = controllerPrivado1;
	}
	

}
