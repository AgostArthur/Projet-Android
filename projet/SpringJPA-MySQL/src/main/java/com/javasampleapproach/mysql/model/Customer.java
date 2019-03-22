package com.javasampleapproach.mysql.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.Gson;

@Entity
@Table(name = "customer")
public class Customer implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "Genre")
	private String genre;

	@Column(name = "prenom")
	private String prenom;

	@Column(name = "nom")
	private String nom;
	
	@Column(name="email")
	private String email;
	
	@Column(name="age")
	private int age;

	protected Customer() {
	}
	
	public Customer(String genre, String prenom, String nom, String email, int age) {
		this.genre = genre;
		this.prenom = prenom;
		this.nom = nom;
		this.email = email;
		this.age = age;
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		String json = gson.toJson(this);
		return json;
	}
}
