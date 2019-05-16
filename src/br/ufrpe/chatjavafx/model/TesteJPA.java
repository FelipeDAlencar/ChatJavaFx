package br.ufrpe.chatjavafx.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import br.ufrpe.chatjavafx.jpa.ConnectionFactory;
import br.ufrpe.chatjavafx.model.dao.DAOUsuario;

public class TesteJPA {
	public static void main(String[] args) {

		DAOUsuario daoUsuario = DAOUsuario.getInstance();
		Usuario usuario = new Usuario("alencar", "123");
		System.out.println(daoUsuario.buscarLogin(usuario));
	}
}
