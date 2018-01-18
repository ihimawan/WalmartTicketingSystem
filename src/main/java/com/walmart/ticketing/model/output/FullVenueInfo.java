package com.walmart.ticketing.model.output;

import java.util.List;

import com.walmart.ticketing.model.Seat;

public class FullVenueInfo {
	
	private String venueId;
	private String venueName;

	private int availableSeats;
	private int holdSeats;
	private int reservedSeats;
	private int totalSeats;
	
	private List<Seat> availableSeatsDetails;
	private List<Seat> holdSeatsDetails;
	private List<Seat> reservedSeatsDetails;
	
	public String getVenueId() {
		return venueId;
	}
	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}
	public String getVenueName() {
		return venueName;
	}
	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}
	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}
	public void setHoldSeats(int holdSeats) {
		this.holdSeats = holdSeats;
	}
	public void setReservedSeats(int reservedSeats) {
		this.reservedSeats = reservedSeats;
	}
	public void setAvailableSeatsDetails(List<Seat> availableSeatsDetails) {
		this.availableSeatsDetails = availableSeatsDetails;
	}
	public void setHoldSeatsDetails(List<Seat> holdSeatsDetails) {
		this.holdSeatsDetails = holdSeatsDetails;
	}
	public void setReservedSeatsDetails(List<Seat> reservedSeatsDetails) {
		this.reservedSeatsDetails = reservedSeatsDetails;
	}
	public int getAvailableSeats() {
		return availableSeats;
	}
	public int getHoldSeats() {
		return holdSeats;
	}
	public int getReservedSeats() {
		return reservedSeats;
	}
	public List<Seat> getAvailableSeatsDetails() {
		return availableSeatsDetails;
	}
	public List<Seat> getHoldSeatsDetails() {
		return holdSeatsDetails;
	}
	public List<Seat> getReservedSeatsDetails() {
		return reservedSeatsDetails;
	}
	public int getTotalSeats() {
		return availableSeats + holdSeats + reservedSeats;
	}
	public void setTotalSeats(int totalSeats) {
		this.totalSeats = availableSeats + holdSeats + reservedSeats;
	}

}
