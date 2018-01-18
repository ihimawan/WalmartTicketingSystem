package com.walmart.ticketing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walmart.ticketing.model.Customer;
import com.walmart.ticketing.repository.CustomerRepository;

@Service
public class CustomerService {
	
	@Autowired
	CustomerRepository custRepo;

	//tries to find customer by email. If not found, then will create the user automatically.
	public Customer findCustomerByEmail(String customerEmail) {
		List<Customer> res = custRepo.findByEmail(customerEmail);
		if (res.size() == 0) {
			return addCustomer(customerEmail);
		}
		
		return res.get(0);
	}
	
	public Customer addCustomer(String customerEmail)
	{
		return custRepo.save(new Customer(customerEmail));
	}
	
	public String getEmailById(String customerId) {
		return custRepo.findOne(customerId).getEmail();
	}
}
