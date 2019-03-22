package com.javasampleapproach.mysql.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javasampleapproach.mysql.model.Customer;
import com.javasampleapproach.mysql.repo.CustomerRepository;

@RestController
public class WebController {
	@Autowired
	CustomerRepository repository;
	
	@RequestMapping("customer/add")
	public String add(@RequestParam("genre") String genre,
			@RequestParam("prenom") String prenom,
			@RequestParam("nom") String nom,
			@RequestParam("email") String email,
			@RequestParam("age") int age){
		if (repository.findByEmail(email).isEmpty()) {
			repository.save(new Customer(genre, prenom, nom, email, age));
			return "true";
		}
		else {
			return "false";
		}
	}
	
	
	@RequestMapping("customer/findall")
	public String findAll(){
		String result = "<html>";
		
		for(Customer cust : repository.findAll()){
			result += "<div>" + cust.toString() + "</div>";
		}
		
		return result + "</html>";
	}
	
	//@RequestMapping("/findbyid")
	//public String findById(@RequestParam("id") long id){
	//	String result = "";
	//	result = repository.findOne(id).toString();
	//	return result;
	//}
	
	@RequestMapping("customer/findbyemail")
	public String fetchDataByLastName(@RequestParam("email") String email){
		
		if (repository.findByEmail(email).isEmpty()) {
			return "false";
		}
		else {
			return "true";
		}
	}
}