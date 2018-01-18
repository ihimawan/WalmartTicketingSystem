package com.walmart.ticketing.model;

import java.util.ArrayList;
import java.util.Date;
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
@Table(name="seat_group_hold")
public class SeatGroupHold {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	@Formula("select count(*) from Seat s where s.seat_hold_id = id")
	private int numberOfSeats;
	
	@OneToMany(mappedBy = "seatGroupHoldId", cascade = CascadeType.PERSIST)
	private List<Seat> seatId = new ArrayList<Seat>();
	
	private Date createTime;

	@ManyToOne
	@JoinColumn(name="customer_id")
	private Customer customerId;
	
	private boolean claimed;
	
	
	public int getNumberOfSeats() {
		return numberOfSeats;
	}

	public void setNumberOfSeats(int numberOfSeats) {
		this.numberOfSeats = numberOfSeats;
	}

	public boolean isClaimed() {
		return claimed;
	}

	public void setClaimed(boolean claimed) {
		this.claimed = claimed;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@JsonManagedReference
	public List<Seat> getSeatId() {
		return seatId;
	}

	public void setSeatId(List<Seat> seatId) {
		this.seatId = seatId;
	}

	public Customer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Customer customerId) {
		this.customerId = customerId;
	}
}
