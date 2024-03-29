package br.ufrpe.chatjavafx.model.dao;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.ufrpe.chatjavafx.jpa.ConnectionFactory;
import br.ufrpe.chatjavafx.model.MensagensDaSala;
import br.ufrpe.chatjavafx.model.Usuario;
import br.ufrpe.chatjavafx.view.Alerta;
import javafx.scene.control.Alert.AlertType;

public class DAOMsgDaSala {
	private EntityManager em;
	private static DAOMsgDaSala daoMsgDaSala;

	private DAOMsgDaSala() {
		em = ConnectionFactory.getInstance().getConnection();
	}
	
	public static DAOMsgDaSala getInstance() {
		if (daoMsgDaSala != null) {
			return daoMsgDaSala;
		}

		return daoMsgDaSala = new DAOMsgDaSala();
	}
	
	public void salvar(MensagensDaSala msgDaSala) {
		EntityManager em = ConnectionFactory.getInstance().getConnection();
		try {
			em.getTransaction().begin();
			if (msgDaSala.getId() != null) {
				em.merge(msgDaSala);
			} else {
				em.persist(msgDaSala);
			}
			em.getTransaction().commit();

		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			Alerta alerta = Alerta.getInstace(null);
			alerta.alertar(AlertType.ERROR, "Erro", "Erro ao tentar inserir no banco de dados",
					"Ocorreu um erro ao tentar inserir no banco de dados. Por favor tente novamente.");
		} finally {
			em.close();
		}
	}
	
	public ArrayList<MensagensDaSala> buscarTodos() {
		EntityManager em = ConnectionFactory.getInstance().getConnection();

		ArrayList<MensagensDaSala> msgDaSala = null;
		try {

			msgDaSala = (ArrayList<MensagensDaSala>) em.createQuery("from MensagensDaSala m").getResultList();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			em.close();
		}
		return msgDaSala;
	}
	
	public ArrayList<MensagensDaSala> buscarMsgOffline(Usuario usuario) {
		EntityManager em = ConnectionFactory.getInstance().getConnection();

		ArrayList<MensagensDaSala> msgDaSala = null;
		try {

			TypedQuery<MensagensDaSala> query = em
					.createQuery("select m from MensagensDaSala m where m.usuarioOff = :usuarioOff", MensagensDaSala.class);
			query.setParameter("usuarioOff", usuario);
			
			msgDaSala = (ArrayList<MensagensDaSala>) query.getResultList();
			return msgDaSala;
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			em.close();
		}
		return msgDaSala;
	}
	
	public void deletarMsgVisulizadaSala(Usuario usuario) {
		EntityManager em = ConnectionFactory.getInstance().getConnection();

		
		try {
			
			em.getTransaction().begin();
			Query query = em
					.createQuery("delete from MensagensDaSala m where m.usuarioOff = :usuarioOff");
			query.setParameter("usuarioOff", usuario).executeUpdate();
			em.getTransaction().commit();
			
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			em.close();
		}
		
	}
}
