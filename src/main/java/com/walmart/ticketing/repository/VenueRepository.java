package com.walmart.ticketing.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.walmart.ticketing.model.Venue;

public interface VenueRepository extends CrudRepository<Venue, String>{

	public List<Venue> findById(String id);
	
}
