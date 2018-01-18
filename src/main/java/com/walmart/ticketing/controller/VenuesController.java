package com.walmart.ticketing.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.walmart.ticketing.exceptions.ResourceNotFoundException;
import com.walmart.ticketing.model.Venue;
import com.walmart.ticketing.model.output.FullVenueInfo;
import com.walmart.ticketing.service.SeatGroupHoldService;
import com.walmart.ticketing.service.SeatGroupReservedService;
import com.walmart.ticketing.service.TicketServiceImpl;
import com.walmart.ticketing.service.VenueService;

@RestController
@RequestMapping("/api/venues")
public class VenuesController{
	
	@Autowired
	VenueService venueService;
	
	@Autowired
	SeatGroupHoldService sghService;
	
	@Autowired
	SeatGroupReservedService sgrService;
	
	@Autowired
	TicketServiceImpl ticketService;
	
	@GetMapping(value="")
	public List<Venue> getAllVenues() {
		return venueService.getAllVenues();
	}

	//given ID of venue, will display number of seats available (not held, not reserved)
	@GetMapping(value="{id}")
	public FullVenueInfo getVenueInfo(@PathVariable("id") String id) throws Exception {
		FullVenueInfo fvi =  venueService.getVenueInfo(id);
		
		if (fvi==null) {
			throw new ResourceNotFoundException("Venue with id="+id+" isn't found.");
		}else {
			return fvi;
		}
	}
	
	public void something() {
		System.out.println("----");
	}
	
	//given ID of venue, will display number of seats available (not held, not reserved)
	@GetMapping(value="{id}/availableSeats")
	public int getVenueAvailableSeats(@PathVariable("id") String id) throws Exception {
		Integer numAvail = ticketService.numSeatsAvailable(id);
		if (numAvail==null) {
			throw new ResourceNotFoundException("Venue with id="+id+" isn't found.");
		}else {
			return numAvail;
		}
	}
	
}

