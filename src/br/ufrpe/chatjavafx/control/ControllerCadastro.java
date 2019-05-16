package br.ufrpe.chatjavafx.control;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import br.ufrpe.chatjavafx.model.Cliente;
import br.ufrpe.chatjavafx.view.Alerta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;

public class ControllerCadastro implements Initializable {
	private static final String CASDATRAR = "--CASDATRAR--";

	@FXML
	private JFXTextField tfLogin;

	@FXML
	private JFXPasswordField tfSenha;

	@FXML
	private JFXPasswordField tfConfirmarSenha;

	@FXML
	private JFXButton btnConfirmar;

	private Cliente cliente;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@FXML
	void acaoBtnConfirmar(ActionEvent event) {
		String login = tfLogin.getText();
		String senha = tfSenha.getText();
		String confimarSenha = tfConfirmarSenha.getText();

		if (confimarSenha.equals(senha)) {
			cliente.enviarMensagem(login + " - " + senha + " - " + CASDATRAR);
		} else {
			Alerta alerta = Alerta.getInstace(null);
			alerta.alertar(AlertType.INFORMATION, "Atenção", "Senhas não correspodem",
					"Certifique-se que as senhas são iguais");
		}

	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}
