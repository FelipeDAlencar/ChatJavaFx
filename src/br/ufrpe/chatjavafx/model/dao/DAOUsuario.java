package br.ufrpe.chatjavafx.model.dao;

import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.ufrpe.chatjavafx.jpa.ConnectionFactory;
import br.ufrpe.chatjavafx.model.Usuario;
import br.ufrpe.chatjavafx.view.Alerta;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DAOUsuario {
	private EntityManager em;
	private static DAOUsuario daoUsuario;

	private DAOUsuario() {
		em = ConnectionFactory.getInstance().getConnection();
	}

	public static DAOUsuario getInstance() {
		if (daoUsuario != null) {
			return daoUsuario;
		}

		return daoUsuario = new DAOUsuario();
	}

	public void salvar(Usuario usuario) {
		EntityManager em = ConnectionFactory.getInstance().getConnection();
		try {
			em.getTransaction().begin();
			if (usuario.getId() != null) {
				em.merge(usuario);
			} else {
				em.persist(usuario);
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

	public ArrayList<Usuario> buscarTodos() {
		EntityManager em = ConnectionFactory.getInstance().getConnection();

		ArrayList<Usuario> usuarios = null;
		try {

			usuarios = (ArrayList<Usuario>) em.createQuery("from Usuario u").getResultList();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			em.close();
		}

		return usuarios;
	}

	public Usuario buscarLogin(Usuario usuario) {
		EntityManager em = ConnectionFactory.getInstance().getConnection();

		try {

			TypedQuery<Usuario> query = em
					.createQuery("select u from Usuario u where u.login = :login and u.senha = :senha", Usuario.class);
			query.setParameter("login", usuario.getLogin());
			query.setParameter("senha", usuario.getSenha());
			Usuario resuUsuario;
			return resuUsuario = query.getSingleResult();

		} catch (NoResultException e) {
			e.printStackTrace();
			Alerta alerta = Alerta.getInstace(Alert.AlertType.NONE);
			alerta.alertar(Alert.AlertType.INFORMATION, "Erro.", "Problema ao logar.", "Login ou senha inválidos.");
		} finally {
			em.close();
		}
		return null;
	}

	public Usuario buscarLoginIgual(Usuario usuario) {
		EntityManager em = ConnectionFactory.getInstance().getConnection();

		try {

			TypedQuery<Usuario> query = em.createQuery("select u from Usuario u where u.login = :login", Usuario.class);
			query.setParameter("login", usuario.getLogin());
			Usuario resuUsuario;
			return resuUsuario = query.getSingleResult();
		} catch (NoResultException e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
		return null;
	}

	public ArrayList<Usuario> buscarLogados() {
		EntityManager em = ConnectionFactory.getInstance().getConnection();

		ArrayList<Usuario> usuarios = null;
		try {

			usuarios = (ArrayList<Usuario>) em.createQuery("from Usuario u where u.logado = true").getResultList();
			return usuarios;
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			em.close();
		}

		return usuarios;
	}

	public ArrayList<Usuario> buscarUsuariosOff() {
		EntityManager em = ConnectionFactory.getInstance().getConnection();

		ArrayList<Usuario> usuarios = null;
		try {

			usuarios = (ArrayList<Usuario>) em.createQuery("from Usuario u where u.logado = false").getResultList();
			return usuarios;
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			em.close();
		}

		return usuarios;
	}

	public Usuario buscarPeloLogin(String login) {
		EntityManager em = ConnectionFactory.getInstance().getConnection();

		Usuario usuario = null;
		try {

			TypedQuery<Usuario> query = em.createQuery("select u from Usuario u where u.login = :login", Usuario.class);
			query.setParameter("login", login);
			usuario = query.getSingleResult();
			return usuario;
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			em.close();
		}

		return usuario;
	}
}
