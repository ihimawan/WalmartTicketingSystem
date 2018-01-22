package com.walmart.ticketing.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
	private static Logger log = Logger.getLogger(SeatReservedController.class);

	@Autowired
	SeatGroupReservedService sgrService;
	
	@Autowired
	TicketServiceImpl ticketService;
	
	@GetMapping(value="")
	public List<SeatGroupReserved> getAllHolds(){
		return sgrService.getAllSeatGroupReserves();
	}
	
	@GetMapping(value="{id}")
	public SeatGroupReserved getHoldSeatId(@PathVariable("id") String id) {
		SeatGroupReserved sgr = sgrService.getSeatGroupReservedById(id);
		
		if (sgr==null) {
			throw new ResourceNotFoundException("Reservation with id="+id+" isn't found.");
		}else {
			return sgr;
		}
	}
	
	//given 'seatHoldId' reserve them
	@PostMapping(value="")
	public ResponseMessage reserveSeats(@RequestParam("seatHoldId") String seatHoldId, 
							@RequestParam("email") String customerEmail,
							HttpServletRequest request){
		
		ResponseMessage rm = null;
		
		try {
			
			String reservationId = ticketService.reserveSeats(seatHoldId, customerEmail);
			rm = new ResponseMessage (Status.SUCCESS, "Reserved with id=" + reservationId);
			String linkUrl = String.format("%s://%s:%d/api/seatReserves/%s",request.getScheme(),  request.getServerName(), request.getServerPort(), reservationId);
			rm.setLink(linkUrl);
			log.info("Reservations has been made based on seatHoldId="+seatHoldId+". Resulting reservation ID=" + reservationId);
		} catch (Exception e) {
			rm = new ResponseMessage (Status.FAILURE, e.getMessage());
		}
		
		return rm;
	}
	
}
