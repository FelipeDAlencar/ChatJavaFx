package br.ufrpe.chatjavafx.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "msgprivado_sequence", sequenceName = "msgprivado_seq", initialValue = 1, allocationSize = 1)
public class MensagenPrivadas {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "msgprivado_sequence")
	private Integer id;
	private String msg;
	@ManyToOne
	private Usuario loginRemetente;
	@ManyToOne
	private Usuario loginDestinatario;	
	
	public MensagenPrivadas(Usuario loginRemetente, Usuario loginDestinario,String msg) {
		this.msg = msg;
		this.loginDestinatario = loginDestinario;
		this.loginRemetente = loginRemetente;
	}
	
	public MensagenPrivadas() {
		
	};
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Usuario getLoginRemetente() {
		return loginRemetente;
	}

	public void setLoginRemetente(Usuario loginRemetente) {
		this.loginRemetente = loginRemetente;
	}

	public Usuario getLoginDestinatario() {
		return loginDestinatario;
	}

	public void setLoginDestinatario(Usuario loginDestinatario) {
		this.loginDestinatario = loginDestinatario;
	}
	
	
	
	
}
