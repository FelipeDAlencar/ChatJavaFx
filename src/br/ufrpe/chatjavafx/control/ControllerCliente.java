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
	private ListView<Usuario> lvOlnine;

	@FXML
	private Label lbNome;

	@FXML
	private Stage meuStage;

	@FXML
	private JFXTabPane tabPane;

	private Cliente cliente;
	private String nome, ip, porta;
	private ControllerCliente controllerClientePrivado;
	private static FXMLLoader loader;
	private ServerSocket serverSocket;
	private int portaPrivada;
	public static ArrayList<ControllerCliente> controllerClientes = new ArrayList<>();
	private DAOUsuario daoUsuario;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cliente = new Cliente(lbNome, lbDigitando, taTexto, tfMsg);
		daoUsuario = DAOUsuario.getInstance();

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
		
		
		Task<Void> task = new Task<Void>() {
			
			@Override
			protected Void call() throws Exception {
				
				while(true) {
					ObservableList<Usuario> observableList = FXCollections.observableArrayList(daoUsuario.buscarLogados());

					for (ControllerCliente controllerCliente2 : ControllerCliente.controllerClientes) {
						controllerCliente2.getLvOlnine().setItems(observableList);
					}
					Thread.sleep(1000);
				}
			}
		};
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();

	}

	private void selecionouDoTv(Usuario usuario) {
		if (usuario != null) {
			
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
		try {
			Tab tab = new Tab(controllerClientePrivado.getNome());
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/br/ufrpe/chatjavafx/view/Privado.fxml"));
			Parent root = loader.load();
			tab.setContent(root);
			tabPane.getTabs().addAll(tab);
			tabPane.getSelectionModel().select(tab);
			


			ControllerPrivado controllerPrivado1 = loader.getController();
			controllerPrivado1.getCliente().setIp(ip);
			controllerPrivado1.getCliente().setPorta(portaPrivada + "");

			FXMLLoader loader2 = new FXMLLoader();
			loader2.setLocation(getClass().getResource("/br/ufrpe/chatjavafx/view/Privado.fxml"));
			Parent outro = loader2.load();
			Tab tabOutro = new Tab(getNome());
			tabOutro.setContent(outro);
			ControllerPrivado controllerPrivado = loader2.getController();
			
			controllerClientePrivado.getTabPane().getTabs().add(tabOutro);
			controllerClientePrivado.getTabPane().getSelectionModel().select(tabOutro);
			
			controllerPrivado.getCliente().setIp(ip);
			controllerPrivado.getCliente().setPorta(portaPrivada + "");
			
			controllerPrivado1.getCliente().conectar();
			controllerPrivado.getCliente().conectar();
			
			ComunicacaoPrivada.privados.add(controllerPrivado1.getCliente().getBfw());
			ComunicacaoPrivada.privados.add(controllerPrivado.getCliente().getBfw());

		} catch (IOException e) {
			Alerta alerta = Alerta.getInstace(null);
			alerta.alertar(AlertType.WARNING, "Atenção", "Atenção", "Erro ao tentar carregar arquivo!");
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

	public ListView<Usuario> getLvOlnine() {
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

	public JFXTabPane getTabPane() {
		return tabPane;
	}

	public int getPortaPrivada() {
		return portaPrivada;
	}

	public void setPortaPrivada(int portaPrivada) {
		this.portaPrivada = portaPrivada;
	}
	

}
