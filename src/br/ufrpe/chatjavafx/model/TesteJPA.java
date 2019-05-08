package br.ufrpe.chatjavafx.model;

import javax.persistence.EntityManager;

import br.ufrpe.chatjavafx.jpa.ConnectionFactory;
import br.ufrpe.chatjavafx.model.dao.DAOUsuario;

public class TesteJPA {
	public static void main(String[] args) {
		
		DAOUsuario daoUsuario = DAOUsuario.getInstance();
		
		System.out.println(daoUsuario.buscarLogados());
		
//		em.getTransaction().begin();
//		em.persist(usuario);
//		em.getTransaction().commit();
//		em.close();
	}
}
