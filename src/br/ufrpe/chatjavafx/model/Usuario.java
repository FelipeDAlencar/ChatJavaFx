package br.ufrpe.chatjavafx.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "usuario_sequence", sequenceName = "usuario_seq", initialValue = 1, allocationSize = 1)
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_sequence")
	private Integer id;
	private String login;
	private String senha;
	private boolean logado;
	
	public Usuario() {
		// TODO Auto-generated constructor stub
	}
	public Usuario(String login, String senha) {
		this.login = login;
		this.senha = senha;
	}
	@Override
	public String toString() {
		
		if(logado) {
			return "+" + login;
		}
		return "-" + login;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public boolean isLogado() {
		return logado;
	}

	public void setLogado(boolean logado) {
		this.logado = logado;
	}

}
