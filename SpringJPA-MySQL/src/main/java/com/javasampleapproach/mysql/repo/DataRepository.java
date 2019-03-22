package com.javasampleapproach.mysql.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.javasampleapproach.mysql.model.Data;

public interface DataRepository extends CrudRepository<Data, Long>{
	List<Data> findByEmail(String email);
}