package br.ufrpe.chatjavafx.view;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Alerta {

	private static Alerta alerta;
	private static Alert alert;

	public static Alerta getInstace() {
		if (!(alerta != null)) {
			alert = new Alert(null);
			alerta = new Alerta();
			return alerta;
		}
		return alerta;
	}

	public void alertar(AlertType tipo, String titulo, String conteudo, String cabecalho) {

		alert.setAlertType(AlertType.INFORMATION);
		alert.setTitle(titulo);
		alert.setContentText(conteudo);
		alert.setHeaderText(cabecalho);
		alert.show();
	}
}
