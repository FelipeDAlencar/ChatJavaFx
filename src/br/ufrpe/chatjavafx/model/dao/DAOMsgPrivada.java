package br.ufrpe.chatjavafx.model.dao;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.ufrpe.chatjavafx.jpa.ConnectionFactory;
import br.ufrpe.chatjavafx.model.MensagenPrivadas;
import br.ufrpe.chatjavafx.model.MensagensDaSala;
import br.ufrpe.chatjavafx.model.Usuario;
import br.ufrpe.chatjavafx.view.Alerta;
import javafx.scene.control.Alert.AlertType;

public class DAOMsgPrivada {
	private EntityManager em;
	private static DAOMsgPrivada daoMsgPrivada;

	private DAOMsgPrivada() {
		em = ConnectionFactory.getInstance().getConnection();
	}

	public static DAOMsgPrivada getInstance() {
		if (daoMsgPrivada != null) {
			return daoMsgPrivada;
		}

		return daoMsgPrivada = new DAOMsgPrivada();
	}

	public void salvar(MensagenPrivadas msgPrivada) {
		EntityManager em = ConnectionFactory.getInstance().getConnection();
		try {
			em.getTransaction().begin();
			if (msgPrivada.getId() != null) {
				em.merge(msgPrivada);
			} else {
				em.persist(msgPrivada);
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

	public ArrayList<MensagenPrivadas> buscarMsgOffline(Usuario usuario) {
		EntityManager em = ConnectionFactory.getInstance().getConnection();

		ArrayList<MensagenPrivadas> msgPrivada = null;
		try {

			TypedQuery<MensagenPrivadas> query = em.createQuery(
					"select m from MensagenPrivadas m where m.loginDestinatario = :usuarioOff", MensagenPrivadas.class);
			query.setParameter("usuarioOff", usuario);

			msgPrivada = (ArrayList<MensagenPrivadas>) query.getResultList();
			return msgPrivada;
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			em.close();
		}
		return msgPrivada;
	}

}
