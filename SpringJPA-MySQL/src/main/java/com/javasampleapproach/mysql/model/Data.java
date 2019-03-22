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
@Table(name = "data")
public class Data implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "transport")
	private String transport;
	
	@Column(name = "vitesse")
	private float vitesse;
	
	@Column(name = "longitude")
	private float longitude;
	
	@Column(name = "latitude")
	private float latitude;
	
	@Column(name = "altitude")
	private float altitude;
	
	@Column(name = "accelerationX")
	private float accelerationX;
	
	@Column(name = "accelerationY")
	private float accelerationY;
	
	@Column(name = "accelerationZ")
	private float accelerationZ;
	
	@Column(name = "normeAcceleration")
	private float normeAcceleration;
	
	@Column(name = "date")
	private String date;
	
	protected Data() {
	}

	public Data(String email, String transport, float vitesse, float longitude, float latitude, float altitude,
			float accelerationX, float accelerationY, float accelerationZ, float normeAcceleration, String date) {
		this.email = email;
		this.transport = transport;
		this.vitesse = vitesse;
		this.longitude = longitude;
		this.latitude = latitude;
		this.altitude = altitude;
		this.accelerationX = accelerationX;
		this.accelerationY = accelerationY;
		this.accelerationZ = accelerationZ;
		this.normeAcceleration = normeAcceleration;
		this.date = date;
	}
	
	@Override
	public String toString() {
		Gson gson = new Gson();
		String json = gson.toJson(this);
		return json;
	}
}
