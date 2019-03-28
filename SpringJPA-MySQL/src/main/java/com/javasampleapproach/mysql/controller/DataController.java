package com.javasampleapproach.mysql.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javasampleapproach.mysql.model.Data;
import com.javasampleapproach.mysql.repo.DataRepository;

@RestController
public class DataController {
	@Autowired
	DataRepository repository;
	
	@RequestMapping("data/add")
	public void add(@RequestParam("email") String email,
			@RequestParam("transport") String transport,
			@RequestParam("vitesse") float vitesse,
			@RequestParam("longitude") float longitude,
			@RequestParam("latitude") float latitude,
			@RequestParam("altitude") float altitude,
			@RequestParam("accelerationX") float accelerationX,
			@RequestParam("accelerationY") float accelerationY,
			@RequestParam("accelerationZ") float accelerationZ,
			@RequestParam("normeAcceleration") float normeAcceleration,
			@RequestParam("date") String date){
			repository.save(new Data(email, transport, vitesse, longitude, latitude, altitude, accelerationX, accelerationY, accelerationZ, normeAcceleration, date));
	}
	
	@RequestMapping("data/findall")
	public String findAll(){
		String result = "<html>";
		
		for(Data data : repository.findAll()){
			result += "<div>" + data.toString() + "</div>";
		}
		
		return result + "</html>";
	}
	
	@RequestMapping("data/findbyemail")
	public String findbymai(@RequestParam("email") String email){
		if (repository.findByEmail(email).isEmpty())
			return "false";
		String result = "<html>";
		
		for (Data data : repository.findByEmail(email)) {
			result += "<div>" + data.toString() + "</div>";
		}
		
		return result + "</html>";
	}
}
