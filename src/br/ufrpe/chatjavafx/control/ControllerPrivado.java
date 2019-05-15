package br.ufrpe.chatjavafx.control;

import java.io.BufferedWriter;
import java.net.URL;
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

public class ControllerPrivado implements Initializable {
	public static final String MSG_PRIVADA = "MSG_PRIVADA";
	public static final String DIGITANDO = "--digitando--";
	public static final String NAO_DIGITANDO = "--nao_digitando--";
	public static final String REQUISITAR_PRIVADO = "REQUISITAR_PRIVADO";
	
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

	private Cliente cliente;
	
	private String nome;
	
	private String ClienteDestino;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//cliente = new Cliente(lbNome, lbDigitando, taTexto, tfMsg);

		tfMsg.setOnKeyPressed((evt) -> {
			if (evt.getCode() == KeyCode.ENTER) {

				cliente.enviarMensagem(nome + ": " + tfMsg.getText() + " - " + ClienteDestino + " - " + MSG_PRIVADA);

			} else {

				cliente.enviarMensagem(DIGITANDO + " - " + ClienteDestino + " - " + MSG_PRIVADA);

			}
		});

		tfMsg.setOnKeyReleased((evt) -> {

			cliente.enviarMensagem(NAO_DIGITANDO + " - " + ClienteDestino + " - " + MSG_PRIVADA);

		});
	}

	@FXML
	void acaoBtn(ActionEvent event) {
		if (event.getSource() == btnSair) {
			System.exit(0);
		} else {
			cliente.enviarMensagem(nome + ": " + tfMsg.getText() + " - " + ClienteDestino + " - " + MSG_PRIVADA);
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
		return ClienteDestino;
	}

	public void setClienteDestino(String clienteDestino) {
		ClienteDestino = clienteDestino;
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

	

	
	
	

}
