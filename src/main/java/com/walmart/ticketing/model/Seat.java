package com.walmart.ticketing.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="seat")
public class Seat {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	@NotNull
	private int column;
	
	@NotNull
	private int row;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name="venue_id")
	private Venue venue;
	
	@ManyToOne
	@JoinColumn(name="seat_hold_id")
	private SeatGroupHold seatGroupHoldId;

	@ManyToOne
	@JoinColumn(name="seat_reserved_id")
	private SeatGroupReserved seatGroupReservedId;

	public Seat() {
	}
	
	public Seat(int column, int row, Venue venue) {
		super();
		this.column = column;
		this.row = row;
		this.venue = venue;
	}
	
	@JsonBackReference
	public Venue getVenue() {
		return venue;
	}

	public void setVenue(Venue venue) {
		this.venue = venue;
	}

	@JsonBackReference
	public SeatGroupHold getSeatGroupHoldId() {
		return seatGroupHoldId;
	}

	public void setSeatGroupHoldId(SeatGroupHold seatGroupHoldId) {
		this.seatGroupHoldId = seatGroupHoldId;
	}

	@JsonBackReference
	public SeatGroupReserved getSeatGroupReservedId() {
		return seatGroupReservedId;
	}

	public void setSeatGroupReservedId(SeatGroupReserved seatGroupReservedId) {
		this.seatGroupReservedId = seatGroupReservedId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}
	
}

