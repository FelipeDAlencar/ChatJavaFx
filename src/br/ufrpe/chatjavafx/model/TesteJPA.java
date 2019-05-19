package br.ufrpe.chatjavafx.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import br.ufrpe.chatjavafx.jpa.ConnectionFactory;
import br.ufrpe.chatjavafx.model.dao.DAOMsgDaSala;
import br.ufrpe.chatjavafx.model.dao.DAOUsuario;

public class TesteJPA {
	public static void main(String[] args) {

		DAOMsgDaSala mSala = DAOMsgDaSala.getInstance();

		Usuario usuario = new Usuario();
		usuario.setId(3);
		mSala.deletarMsgVisulizadaSala(usuario);
	}
}
