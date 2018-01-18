package com.walmart.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.walmart.ticketing.model.Seat;

public interface SeatRepository extends CrudRepository<Seat, String> {

	public List<Seat> findByVenueName(String venueName);
	public List<Seat> findBySeatGroupReservedIdIsNullAndSeatGroupHoldIdIsNotNullAndVenueName(String venueName); //for hold
	public List<Seat> findBySeatGroupReservedIdNotNullAndVenueName(String venueName); //for reserved
	public List<Seat> findBySeatGroupHoldIdIsNullAndSeatGroupReservedIdIsNullAndVenueName(String venueName); //available seating
	
	public Seat findByRowAndColumnAndVenueId(int row, int column, String venueId);
	
	@Query("SELECT MAX(column) FROM Seat WHERE venue_id=:venueId")
	public int getMaxColumn(@Param("venueId") String venueId);
	
	@Query("SELECT MAX(row) FROM Seat WHERE venue_id=:venueId")
	public int getMaxRow(@Param("venueId") String venueId);
}
