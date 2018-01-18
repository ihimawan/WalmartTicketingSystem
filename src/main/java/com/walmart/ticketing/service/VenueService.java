package com.walmart.ticketing.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walmart.ticketing.model.Venue;
import com.walmart.ticketing.model.output.FullVenueInfo;
import com.walmart.ticketing.repository.VenueRepository;

@Service
public class VenueService {

	@Autowired
	SeatService seatService;
	
	@Autowired
	VenueRepository venueRepo;
	
	public FullVenueInfo getVenueInfo(String venueId) throws Exception{
		
		Venue v = getVenueById(venueId);
		if (v==null) {
			return null;
		}
		
		FullVenueInfo vi = new FullVenueInfo();
		vi.setVenueId(venueId);
		String venueName = getNameOfVenue(venueId);
		vi.setVenueName(venueName);
		vi.setAvailableSeats(seatService.getAvailableSeatCount(venueName));
		vi.setHoldSeats(seatService.getHoldSeatCount(venueName));
		vi.setReservedSeats(seatService.getReservedSeatCount(venueName));
		
		vi.setAvailableSeatsDetails(seatService.getAvailableSeats(venueName));
		vi.setHoldSeatsDetails(seatService.getHoldSeats(venueName));
		vi.setReservedSeatsDetails(seatService.getReservedSeats(venueName));
		
		return vi;
	}
	
	public Venue getVenueById(String venueId) {
		return venueRepo.findOne(venueId);
	}
	
	public String getVenueNameById(String venueId) throws Exception {
		try {
			return venueRepo.findOne(venueId).getName();
		}catch(NullPointerException npe) {
			throw new Exception("Venue with id="+venueId+" is not found.");
		}
	}
	
	public String getNameOfVenue(String venueId) throws Exception {
		try {
			return venueRepo.findOne(venueId).getName();
		}catch(NullPointerException npe) {
			throw new Exception("Venue with id="+venueId+" is not found.");
		}
	}

	public List<Venue> getAllVenues() {
		List<Venue> allVenues = new ArrayList<Venue>();
		venueRepo.findAll().forEach(allVenues::add);
		return allVenues;
	}
	
}
