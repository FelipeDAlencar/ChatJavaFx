package br.ufrpe.chatjavafx.control;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import br.ufrpe.chatjavafx.model.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class ControllerPrivado implements Initializable {
	public static final String MSG_PRIVADA = "MSG_PRIVADA";
	public static final String DIGITANDO = "--digitando--";
	public static final String NAO_DIGITANDO = "--nao_digitando--";
	public static final String REQUISITAR_PRIVADO = "REQUISITAR_PRIVADO";
	public static final String ULTIMO_ONLINE = "ULTIMO_ONLINE";
	public static final String VISUALIZOU_PRIVADO = "--VISUALIZOU--";

	@FXML
	private JFXTextArea taTexto;

	@FXML
	private JFXTextField tfMsg;

	@FXML
	private JFXButton btnSair;

	@FXML
	private Label lbDigitando;

	@FXML
	private Label lbNome;

	@FXML
	private Label lbUltimoVisualizacao;

	@FXML
	private Label lbVisualizado;

	private Cliente cliente;

	private String nome;

	private String clienteDestino;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// cliente = new Cliente(lbNome, lbDigitando, taTexto, tfMsg);
		
		tfMsg.focusedProperty().addListener((o, old, nval) -> {
			
			cliente.enviarMensagem(clienteDestino + " - " + cliente.getLogin() + " - " + VISUALIZOU_PRIVADO + " - " + MSG_PRIVADA);
			lbVisualizado.setText("");
			if (nval != null) {
				if (nval.booleanValue()) {
					cliente.enviarMensagem(
							" - " + clienteDestino + " - " + "Online" + " - " + ULTIMO_ONLINE + " - " + MSG_PRIVADA);
				} else {
					SimpleDateFormat formato = new SimpleDateFormat("dd/MM/YYY hh:MM:ss");
					cliente.enviarMensagem(" - " + clienteDestino + " - " + "Visto pot ultimo "
							+ formato.format(new Date()) + " - " + ULTIMO_ONLINE + " - " + MSG_PRIVADA);
				}
			}

		});
		tfMsg.setOnKeyPressed((evt) -> {
			if (evt.getCode() == KeyCode.ENTER) {

				cliente.enviarMensagem(nome + ": " + tfMsg.getText() + " - " + clienteDestino + " - " + MSG_PRIVADA);
				tfMsg.setText("");

			} else {

				cliente.enviarMensagem(MSG_PRIVADA + " - " + clienteDestino + " - " + nome + " - " + " - " + DIGITANDO);

			}
		});

		tfMsg.setOnKeyReleased((evt) -> {

			cliente.enviarMensagem(MSG_PRIVADA + " - " + clienteDestino + " - " + " - " + NAO_DIGITANDO);

		});
	}

	@FXML
	void acaoBtn(ActionEvent event) {
		if (event.getSource() == btnSair) {
			System.exit(0);
		} else {
			cliente.enviarMensagem(nome + ": " + tfMsg.getText() + " - " + clienteDestino + " - " + MSG_PRIVADA);
			tfMsg.setText("");
		}
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;

	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getClienteDestino() {
		return clienteDestino;
	}

	public void setClienteDestino(String clienteDestino) {
		this.clienteDestino = clienteDestino;
	}

	public JFXTextArea getTaTexto() {
		return taTexto;
	}

	public void setTaTexto(JFXTextArea taTexto) {
		this.taTexto = taTexto;
	}

	public JFXTextField getTfMsg() {
		return tfMsg;
	}

	public void setTfMsg(JFXTextField tfMsg) {
		this.tfMsg = tfMsg;
	}

	public Label getLbDigitando() {
		return lbDigitando;
	}

	public void setLbDigitando(Label lbDigitando) {
		this.lbDigitando = lbDigitando;
	}

	public Label getLbNome() {
		return lbNome;
	}

	public void setLbNome(Label lbNome) {
		this.lbNome = lbNome;
	}

	public Label getLbUltimoVisualizacao() {
		return lbUltimoVisualizacao;
	}

	public void setLbUltimoVisualizacao(Label lbUltimoVisualizacao) {
		this.lbUltimoVisualizacao = lbUltimoVisualizacao;
	}

	public Label getLbVisualizado() {
		return lbVisualizado;
	}

}
