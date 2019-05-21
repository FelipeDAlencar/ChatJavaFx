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
		DAOUsuario daoUsuario = DAOUsuario.getInstance();
		Usuario usuario = daoUsuario.buscarPeloLogin("teste");
		
		
		
		
		System.out.println(usuario.getId());
		System.out.println(msgPrivada.buscarMsgOffline(usuario));
		

		
		
	}
}
