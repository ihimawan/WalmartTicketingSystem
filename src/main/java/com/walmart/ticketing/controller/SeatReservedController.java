package com.walmart.ticketing.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.walmart.ticketing.exceptions.ResourceNotFoundException;
import com.walmart.ticketing.model.SeatGroupReserved;
import com.walmart.ticketing.model.output.ResponseMessage;
import com.walmart.ticketing.model.output.Status;
import com.walmart.ticketing.service.SeatGroupReservedService;
import com.walmart.ticketing.service.TicketServiceImpl;

@RestController
@RequestMapping("/api/seatReserves")
public class SeatReservedController {

	@Autowired
	SeatGroupReservedService sgrService;
	
	@Autowired
	TicketServiceImpl ticketService;
	
	@RequestMapping(value="", method=RequestMethod.GET)
	public List<SeatGroupReserved> getAllHolds(){
		return sgrService.getAllSeatGroupReserves();
	}
	
	@RequestMapping(value="{id}", method=RequestMethod.GET)
	public SeatGroupReserved getHoldSeatId(@PathVariable("id") String id) {
		SeatGroupReserved sgr = sgrService.getSeatGroupReservedById(id);
		
		if (sgr==null) {
			throw new ResourceNotFoundException("Reservation with id="+id+" isn't found.");
		}else {
			return sgr;
		}
	}
	
	//given 'seatHoldId' reserve them
	@RequestMapping(value="", method=RequestMethod.POST)
	public ResponseMessage reserveSeats(@RequestParam("seatHoldId") String seatHoldId, 
							@RequestParam("email") String customerEmail,
							HttpServletRequest request){
		
		ResponseMessage rm = null;
		
		try {
			
			String reservationId = ticketService.reserveSeats(seatHoldId, customerEmail);
			rm = new ResponseMessage (Status.SUCCESS, "Reserved with id=" + reservationId);
			String linkUrl = String.format("%s://%s:%d/api/seatReserves/%s",request.getScheme(),  request.getServerName(), request.getServerPort(), reservationId);
			rm.setLink(linkUrl);
			
		} catch (Exception e) {
			rm = new ResponseMessage (Status.FAILURE, e.getMessage());
		}
		
		return rm;
	}
	
}
