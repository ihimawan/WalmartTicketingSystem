package com.walmart.ticketing.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
public class VenuesController {
	
	@Autowired
	VenueService venueService;
	
	@Autowired
	SeatGroupHoldService sghService;
	
	@Autowired
	SeatGroupReservedService sgrService;
	
	@Autowired
	TicketServiceImpl ticketService;
	
	@RequestMapping(value="", method=RequestMethod.GET)
	public List<Venue> getAllVenues() {
		return venueService.getAllVenues();
	}

	//given ID of venue, will display number of seats available (not held, not reserved)
	@RequestMapping(value="{id}", method=RequestMethod.GET)
	public FullVenueInfo getVenueInfo(@PathVariable("id") String id) throws Exception {
		FullVenueInfo fvi =  venueService.getVenueInfo(id);
		
		if (fvi==null) {
			throw new ResourceNotFoundException("Venue with id="+id+" isn't found.");
		}else {
			return fvi;
		}
	}
	
	//given ID of venue, will display number of seats available (not held, not reserved)
	@RequestMapping(value="{id}/availableSeats", method=RequestMethod.GET)
	public int getVenueAvailableSeats(@PathVariable("id") String id) throws Exception {
		Integer numAvail = ticketService.numSeatsAvailable(id);
		if (numAvail==null) {
			throw new ResourceNotFoundException("Venue with id="+id+" isn't found.");
		}else {
			return numAvail;
		}
	}
	
}

