package com.walmart.ticketing.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="customer")
public class Customer {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	@NotNull
	private String email;
	
	@OneToMany(mappedBy="customerId", cascade = CascadeType.ALL)
	private List<SeatGroupHold> seatGroupHolds = new ArrayList<SeatGroupHold>();
	
	@OneToMany(mappedBy="customerId", cascade = CascadeType.ALL)
	private List<SeatGroupReserved> seatGroupReserved = new ArrayList<SeatGroupReserved>();
	
	public Customer(String email) {
		super();
		this.email = email;
	}
	public Customer() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
