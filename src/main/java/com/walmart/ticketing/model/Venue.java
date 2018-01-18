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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="venue")
public class Venue {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	@NotNull
	private String name;
	
	@OneToMany(mappedBy = "venue", cascade = CascadeType.ALL)
	private List<Seat> seats = new ArrayList<Seat>();
	
	public Venue() {
	}
	
	public Venue(String name) {
		super();
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@JsonManagedReference
	@JsonIgnore
	public List<Seat> getSeats() {
		return seats;
	}

	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}
	
}
