package com.walmart.ticketing.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.walmart.ticketing.model.SeatGroupHold;

public interface SeatGroupHoldRepository extends CrudRepository<SeatGroupHold, String>{

	public List<SeatGroupHold> findByClaimed(boolean isClaimed);
	
}
