package com.walmart.ticketing.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="seat_group_reserved")
public class SeatGroupReserved {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	@Formula("select count(*) from Seat s where s.seat_reserved_id = id")
	private int numberOfSeats;
	
	@OneToMany(mappedBy = "seatGroupReservedId", cascade = CascadeType.PERSIST)
	private List<Seat> researvedSeatId = new ArrayList<Seat>();
	
	@ManyToOne
	@JoinColumn(name="customer_id")
	private Customer customerId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNumberOfSeats() {
		return numberOfSeats;
	}

	public void setNumberOfSeats(int numberOfSeats) {
		this.numberOfSeats = numberOfSeats;
	}

	@JsonManagedReference
	public List<Seat> getResearvedSeatId() {
		return researvedSeatId;
	}

	public void setResearvedSeatId(List<Seat> researvedSeatId) {
		this.researvedSeatId = researvedSeatId;
	}

	public Customer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Customer customerId) {
		this.customerId = customerId;
	}
	
	
	
}
