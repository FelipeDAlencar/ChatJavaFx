package br.ufrpe.chatjavafx.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import br.ufrpe.chatjavafx.jpa.ConnectionFactory;
import br.ufrpe.chatjavafx.model.dao.DAOUsuario;

public class TesteJPA {
	public static void main(String[] args) {

		Map<String, String> mapa = new HashMap<>();

		mapa.put("jo", "João Delfino");
		mapa.put("ma", "Maria do Carmo");
		mapa.put("cl", "Claudinei Silva");
		
		System.out.println(mapa.get("jo"));
	}
}
