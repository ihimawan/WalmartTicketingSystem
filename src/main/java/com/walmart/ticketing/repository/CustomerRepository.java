package com.walmart.ticketing.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.walmart.ticketing.model.Customer;

public interface CustomerRepository extends CrudRepository<Customer, String>{

	public List<Customer> findByEmail(String email);
	
}
