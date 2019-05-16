package br.ufrpe.chatjavafx.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "msgsala_sequence", sequenceName = "msgsala_seq", initialValue = 1, allocationSize = 1)
public class MensagensDaSala {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "msgsala_sequence")
	private Integer id;
	private String login;
	private String msg;
	
	
	
	public MensagensDaSala(String login, String msg) {
		this.login = login;
		this.msg = msg;
	}
	
	public MensagensDaSala() {
		
	}
	
	
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
	
	
	

}
