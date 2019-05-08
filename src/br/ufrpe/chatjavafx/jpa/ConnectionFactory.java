package br.ufrpe.chatjavafx.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ConnectionFactory {
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("meuPU");
	private static ConnectionFactory connectionFactory;

	public static ConnectionFactory getInstance() {
		if (connectionFactory != null) {
			return connectionFactory;
		}

		return connectionFactory = new ConnectionFactory();
	}

	public EntityManager getConnection() {
		return emf.createEntityManager();
	}
}
