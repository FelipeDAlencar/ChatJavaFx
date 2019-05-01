package br.ufrpe.chatjavafx.view;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Alerta extends Alert {

	 private static Alerta alert;

	    public static Alerta getInstace(AlertType tipo) {
	        if (alert == null) {
	            return alert = new Alerta(tipo);
	        }

	        return alert;
	    }

	    private Alerta(AlertType alertType) {
	        super(alertType);
	    }

	    public void alertar(AlertType tipo, String titulo, String cabecalho, String conteudo ) {
	        Platform.runLater(()->{
	        	
	        	alert.setAlertType(tipo);
		        alert.setTitle(titulo);
		        alert.setHeaderText(cabecalho);
		        alert.setContentText(conteudo);
		        alert.showAndWait();
		        
	        });
	    }
}
