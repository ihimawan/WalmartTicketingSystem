package com.walmart.ticketing.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.walmart.ticketing.model.Seat;
import com.walmart.ticketing.model.SeatGroupHold;
import com.walmart.ticketing.model.Venue;
import com.walmart.ticketing.repository.SeatGroupHoldRepository;

@Service
public class SeatGroupHoldService {
	
	private static Logger log = Logger.getLogger(SeatGroupHoldService.class);
	
	@Value("${hold.expirationSeconds}")
	private long expiryDuration;

	@Autowired
	SeatGroupHoldRepository sghRepo;
	
	@Autowired
	CustomerService custService;
	
	@Autowired
	SeatService seatService;
	
	public List<SeatGroupHold> getAllSeatGroupHolds(){
		List<SeatGroupHold> allHolds = new ArrayList<SeatGroupHold>();
		sghRepo.findAll().forEach(allHolds::add);
		return allHolds;
	}
	
	public List<Seat> getSeatsByHoldId(String seatHoldId) throws Exception{
		return getSeatGroupHoldById(seatHoldId).getSeatId();
	}
	
	@Transactional
	public SeatGroupHold getSeatGroupHoldById(String seatHoldId){
		return sghRepo.findOne(seatHoldId);
	}
	
	public boolean isSeatHoldValid(SeatGroupHold sgh, Date askedTime) {
		Date createdTime = sgh.getCreateTime();
		return (askedTime.getTime()-createdTime.getTime())<expiryDuration*1000;
	}
	
	@Transactional
	public SeatGroupHold createNewHold(List<String> seatIds, String customerEmail) {
		SeatGroupHold newSeatGroupHold = new SeatGroupHold();
		newSeatGroupHold.setCreateTime(new Date());
		newSeatGroupHold.setCustomerId(custService.findCustomerByEmail(customerEmail));
		List<Seat> seats = new ArrayList<Seat>();
		for (String seatId : seatIds) {
			Seat availableSeat = seatService.getSeatById(seatId);
			seatService.convertAvailableToHold(availableSeat, newSeatGroupHold);
			seats.add(availableSeat);
		}
		newSeatGroupHold.setSeatId(seats);
		newSeatGroupHold.setClaimed(false);
		
		return sghRepo.save(newSeatGroupHold);
	}

	@Transactional
	public SeatGroupHold claim(String seatHoldId, String customerEmail) throws Exception {
		SeatGroupHold sgh = sghRepo.findOne(seatHoldId);
		
		if (sgh == null) {
			throw new Exception("There is no seat hold with id="+seatHoldId);
		}
		
		if (sgh.isClaimed()) {
			throw new Exception("Seat hold with id="+ seatHoldId + " already claimed");
		}
		
		if (!isSeatHoldValid(sgh, new Date())) {
			throw new Exception("Hold already expired"); 
		}
		if (sgh.getCustomerId() != custService.findCustomerByEmail(customerEmail)) {
			throw new Exception(customerEmail + " cannot claim this hold. Expected customer email=" + custService.getEmailById(sgh.getCustomerId().getId())); 
		}
		sgh.setClaimed(true);
		return sghRepo.save(sgh);
	}
	
	//if has expired, refresh the SEAT table 'hold' seats
	@Transactional
	public void refresh() {
		List<SeatGroupHold> unClaimedHolds = sghRepo.findByClaimed(false);
		Date refreshTime = new Date();
		
		for(SeatGroupHold unClaimedHold : unClaimedHolds) {
			boolean isValidHold = isSeatHoldValid(unClaimedHold,refreshTime);
			
			if (!isValidHold) {
				for (Seat holdSeat : unClaimedHold.getSeatId()) {
					seatService.nullifyHoldId(holdSeat);
				}
			}
		}
		log.info("Database has been refreshed.");
	}
	
}

