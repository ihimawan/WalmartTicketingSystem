package com.walmart.ticketing.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walmart.ticketing.model.SeatGroupHold;
import com.walmart.ticketing.model.SeatGroupReserved;
import com.walmart.ticketing.util.Coordinate;
import com.walmart.ticketing.util.SeatSolver;

@Service
public class TicketServiceImpl implements TicketService {

	@Autowired
	VenueService venueService;

	@Autowired
	SeatService seatService;

	@Autowired
	SeatGroupHoldService sghService;

	@Autowired
	SeatGroupReservedService sgrService;

	@Override
	public int numSeatsAvailable(String venueId) throws Exception {
		
		if (venueService.getVenueById(venueId) == null) {
			return -1;
		}
		
		return seatService.getAvailableSeatCount(venueService.getNameOfVenue(venueId));
	}

	@Override
	public SeatGroupHold findAndHoldSeats(int numSeats, String customerEmail, String venueId) throws Exception {
		String venueName = venueService.getNameOfVenue(venueId);
		
		List<Coordinate> bestSeatCoordinates = SeatSolver.getBestSeats(numSeats, seatService.getMaxRow(venueId) + 1,
				seatService.getMaxColumn(venueId) + 1, seatService.getAvailableSeats(venueName));

		List<String> bestSeatsIds = new ArrayList<String>();

		for (Coordinate seatCoor : bestSeatCoordinates) {
			bestSeatsIds.add(seatService.getSeatIdByRowAndColumn(seatCoor.getRow(), seatCoor.getColumn(), venueId));
		}

		return sghService.createNewHold(bestSeatsIds, customerEmail);

	}

	@Override
	public String reserveSeats(String seatHoldId, String customerEmail) throws Exception {
		SeatGroupReserved sgr = sgrService.newReservationFromHold(seatHoldId, customerEmail);
		return sgr.getId();
	}

}
