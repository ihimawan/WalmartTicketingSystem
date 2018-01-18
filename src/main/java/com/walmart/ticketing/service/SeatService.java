package com.walmart.ticketing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.walmart.ticketing.model.Seat;
import com.walmart.ticketing.model.SeatGroupHold;
import com.walmart.ticketing.repository.SeatRepository;

@Service
public class SeatService {
	
	@Value("${hold.expirationSeconds}")
	private long expiryDuration;
	
	@Autowired
	SeatRepository seatRepo;
	
	@Autowired
	SeatGroupHoldService sghService;
	
	public Seat getSeatById(String seatId) {
		return seatRepo.findOne(seatId);
	}
	
	public List<Seat> getAllSeats(String venueName){
		List<Seat> result = seatRepo.findByVenueName(venueName);
		return result;
	}

	public List<Seat> getAvailableSeats(String venueName){
		List<Seat> result = seatRepo.findBySeatGroupHoldIdIsNullAndSeatGroupReservedIdIsNullAndVenueName(venueName);
		return result;
	}
	
	public int getAvailableSeatCount(String venueName) {
		return getAvailableSeats(venueName).size();
	}
	
	public List<Seat> getHoldSeats(String venueName){
		List<Seat> result = seatRepo.findBySeatGroupReservedIdIsNullAndSeatGroupHoldIdIsNotNullAndVenueName(venueName);
		return result;
	}
	
	public int getHoldSeatCount(String venueName) {
		return getHoldSeats(venueName).size();
	}
	
	public List<Seat> getReservedSeats(String venueName){
		List<Seat> result = seatRepo.findBySeatGroupReservedIdNotNullAndVenueName(venueName);
		return result;
	}
	
	public int getReservedSeatCount(String venueName) {
		return getReservedSeats(venueName).size();
	}
	
	public Seat convertAvailableToHold(Seat availableSeat, SeatGroupHold sgh) {
		availableSeat.setSeatGroupHoldId(sgh);
		return seatRepo.save(availableSeat);
	}
	
	//this happens does not happen MANUALLY. This happens once the time has expired
	public void nullifyHoldId(Seat holdSeat) {
		holdSeat.setSeatGroupHoldId(null);
		seatRepo.save(holdSeat);
	}

	public int getMaxRow(String venueId) {
		return seatRepo.getMaxRow(venueId);
	}

	public int getMaxColumn(String venueId) {
		return seatRepo.getMaxColumn(venueId);
	}
	
	public String getSeatIdByRowAndColumn(int row, int column, String venueId) {
		return seatRepo.findByRowAndColumnAndVenueId(row, column, venueId).getId();
	}
	
}
