package br.ufrpe.chatjavafx.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import br.ufrpe.chatjavafx.jpa.ConnectionFactory;
import br.ufrpe.chatjavafx.model.dao.DAOMsgDaSala;
import br.ufrpe.chatjavafx.model.dao.DAOMsgPrivada;
import br.ufrpe.chatjavafx.model.dao.DAOUsuario;

public class TesteJPA {
	public static void main(String[] args) {

		DAOMsgPrivada msgPrivada = DAOMsgPrivada.getInstance();
		
		Usuario usuario = new Usuario();
		usuario.setId(6);
		
		System.out.println(msgPrivada.buscarMsgOffline(usuario));
		

		
		
	}
}
