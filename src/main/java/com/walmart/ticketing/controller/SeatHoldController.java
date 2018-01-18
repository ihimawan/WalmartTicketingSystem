package com.walmart.ticketing.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.walmart.ticketing.exceptions.ResourceNotFoundException;
import com.walmart.ticketing.model.SeatGroupHold;
import com.walmart.ticketing.model.output.ResponseMessage;
import com.walmart.ticketing.model.output.Status;
import com.walmart.ticketing.service.SeatGroupHoldService;
import com.walmart.ticketing.service.TicketServiceImpl;

@RestController
@RequestMapping("/api/seatHolds")
public class SeatHoldController {

	@Autowired
	TicketServiceImpl ticketService;
	
	@Autowired
	SeatGroupHoldService sghService;
	
	@RequestMapping(value="", method=RequestMethod.GET)
	public List<SeatGroupHold> getAllHolds(){
		return sghService.getAllSeatGroupHolds();
	}

	@RequestMapping(value="{id}", method=RequestMethod.GET)
	public SeatGroupHold getHoldSeatId(@PathVariable("id") String id,
										HttpServletResponse hsr) throws Exception {
		
		SeatGroupHold sgh = sghService.getSeatGroupHoldById(id);
		
		if (sgh==null) {
			throw new ResourceNotFoundException("Seat Hold with id="+id+" isn't found.");
		}else {
			return sgh;
		}
	}
	
	@RequestMapping(value="", method=RequestMethod.POST)
	public ResponseMessage test(@RequestParam("numSeats") int numSeats,
								@RequestParam("email") String customerEmail,
								@RequestParam("venueId") String venueId,
								HttpServletRequest request) {

		ResponseMessage rm = null;
		
		try {
			SeatGroupHold newSgh = ticketService.findAndHoldSeats(numSeats, customerEmail, venueId);
			rm = new ResponseMessage(Status.SUCCESS, "Your seatHoldId=" + newSgh.getId());
			String linkUrl = String.format("%s://%s:%d/api/seatHolds/%s",request.getScheme(),  request.getServerName(), request.getServerPort(), newSgh.getId());
			rm.setLink(linkUrl);
		} catch (Exception e) {
			rm = new ResponseMessage(Status.FAILURE, e.getMessage());
			e.printStackTrace();
		}
		return rm;
	}
	
}
