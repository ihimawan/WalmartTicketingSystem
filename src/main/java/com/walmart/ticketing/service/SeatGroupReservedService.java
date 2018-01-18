package com.walmart.ticketing.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walmart.ticketing.model.Customer;
import com.walmart.ticketing.model.Seat;
import com.walmart.ticketing.model.SeatGroupHold;
import com.walmart.ticketing.model.SeatGroupReserved;
import com.walmart.ticketing.repository.SeatGroupReservedRepository;

@Service
public class SeatGroupReservedService {

	@Autowired
	CustomerService custService;

	@Autowired
	SeatGroupHoldService sghService;
	
	@Autowired
	SeatGroupReservedRepository sgrRepo;
	
	@Autowired
	SeatService seatService;

	@Transactional
	public SeatGroupReserved newReservationFromHold(String seatHoldId, String customerEmail) throws Exception {

		//claim the hold
		SeatGroupHold sgh = sghService.claim(seatHoldId,customerEmail);

		// find customer ID
		Customer c = custService.findCustomerByEmail(customerEmail);
		
		// create new entry for reserved
		SeatGroupReserved sgr = new SeatGroupReserved();
		sgr.setCustomerId(c);
		
		// for seat number seatHoldId in the SEAT_GROUP_HOLD table,
		// get its associations to all the SEATS.
		List<Seat> heldSeats = sgh.getSeatId();
		
		List<Seat> reservedSeats = new ArrayList<Seat>();
		for (Seat s : heldSeats) {
			s.setSeatGroupReservedId(sgr);
			reservedSeats.add(s);
		}

		sgr.setResearvedSeatId(reservedSeats);

		return sgrRepo.save(sgr);
	}

	public SeatGroupReserved getSeatGroupReservedById(String id) {
		return sgrRepo.findOne(id);
		
	}

	public List<SeatGroupReserved> getAllSeatGroupReserves() {
		List<SeatGroupReserved> allReserves = new ArrayList<SeatGroupReserved>();
		sgrRepo.findAll().forEach(allReserves::add);
		return allReserves;
	}

}
