	package com.walmart.ticketing.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.walmart.ticketing.WalmartTicketingSystemApplication;
import com.walmart.ticketing.model.SeatGroupHold;
import com.walmart.ticketing.model.SeatGroupReserved;
import com.walmart.ticketing.repository.SeatGroupHoldRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes=WalmartTicketingSystemApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TicketServiceTest {

	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private SeatGroupReservedService sgrService;
	
	@Autowired
	private SeatGroupHoldService sghService;
	
	@Autowired
	private SeatGroupHoldRepository sghRepo;
	
	private static String createdSeatGroupHoldId;
	
	@Autowired
	private Environment env;
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testA_NumSeatsAvailable_NoExistVenue() throws Exception {
		int numSeatsAvailable = ticketService.numSeatsAvailable("1000");
		assertEquals(-1, numSeatsAvailable);
	}
	
	@Test
	public void testB_NumSeatsAvailable_ExistVenue() throws Exception {
		int numSeatsAvailable = ticketService.numSeatsAvailable("1");
		assertEquals(36, numSeatsAvailable);
	}
	
	@Test
	public void testC_FindAndHoldSeats_noExistVenueId() throws Exception{
		String venueId = "1000";
		try {
			ticketService.findAndHoldSeats(1, "test@test.com", venueId);
			fail("Exception was expected.");
		}catch(Exception e) {
			assertEquals("Venue with id="+venueId+" is not found.", e.getMessage());
		}
	}
	
	@Test
	public void testD_FindAndHoldSeats_tooBigRequest() throws Exception{
		int numOfSeats = 1000;
		String venueId = "1";
		try {
			ticketService.findAndHoldSeats(numOfSeats, "test@test.com", venueId);
			fail("Exception was expected.");
		}catch(Exception e) {
			assertEquals("Unable to get best seats. There are only " + ticketService.numSeatsAvailable(venueId)
			+ " available while " + numOfSeats + " is requested.", e.getMessage());
		}
	}
	
	@Test
	public void testE_FindAndHoldSeats_tooLittleRequest() throws Exception{
		int numOfSeats = -1;
		String venueId = "1";
		try {
			ticketService.findAndHoldSeats(numOfSeats, "test@test.com", venueId);
			fail("Exception was expected.");
		}catch(Exception e) {
			assertEquals(numOfSeats + " number of seats cannot be booked.", e.getMessage());
		}
	}
	
	@Test
	public void testF_FindAndHoldSeats_success() throws Exception{
		int numOfSeats = 36;
		String venueId = "1";
		String email = "test@test.com";
		SeatGroupHold sgh = ticketService.findAndHoldSeats(numOfSeats, email, venueId);
		
		assertNotNull(sghService.getSeatGroupHoldById(sgh.getId()));
		
		assertEquals(numOfSeats,sgh.getSeatId().size());
		assertNotNull(sgh.getCreateTime());
		assertFalse(sgh.isClaimed());
		assertEquals(email, sgh.getCustomerId().getEmail());
		
		createdSeatGroupHoldId = sgh.getId();
	}
	
	@Test
	public void testG_reserveSeats_noExistSeatHold() throws Exception{
		String seatHoldId = "1000";
		String email = "test@test.com";
		try {
			ticketService.reserveSeats(seatHoldId, email);
			fail("Exception was expected.");
		}catch(Exception e) {
			assertEquals("There is no seat hold with id="+seatHoldId, e.getMessage());
		}
	}
	
	@Test
	public void testH_reserveSeats_wrongEmail() throws Exception{
		String seatHoldId = createdSeatGroupHoldId;
		String email = "wrong@test.com";
		System.out.println(seatHoldId);
		String correctEmail = sghService.getSeatGroupHoldById(seatHoldId).getCustomerId().getEmail();
		try {
			ticketService.reserveSeats(seatHoldId, email);
			fail("Exception was expected.");
		}catch(Exception e) {
			assertEquals(email + " cannot claim this hold. Expected customer email="+correctEmail, e.getMessage());
		}
	}
	
	@Test
	public void testI_reserveSeats_expiredHold() throws Exception{
		String email= "test@test.com";
		SeatGroupHold sgh = ticketService.findAndHoldSeats(1, email, "2");
		Date createTime = sgh.getCreateTime();
		
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(createTime);
		int expiryDuration = Integer.parseInt(env.getProperty("hold.expirationSeconds"));
		calendar.set(Calendar.SECOND,(calendar.get(Calendar.SECOND)-expiryDuration));
		sgh.setCreateTime(calendar.getTime());
		System.out.println(createTime);
		System.out.println(calendar.getTime());
		sghRepo.save(sgh);
		
		try {
			ticketService.reserveSeats(sgh.getId(), email);
			fail("Exception was expected.");
		}catch(Exception e) {
			assertEquals("Hold already expired", e.getMessage());
		}
	}
	
	@Test
	public void testJ_reserveSeats_success() throws Exception{
		String seatHold = createdSeatGroupHoldId;
		String email = "test@test.com";
		String reserveId = ticketService.reserveSeats(seatHold, email);
		SeatGroupReserved sgr = sgrService.getSeatGroupReservedById(reserveId);
		SeatGroupHold sgh = sghService.getSeatGroupHoldById(seatHold);
		
		assertTrue(sgh.isClaimed());
		assertEquals(email, sgr.getCustomerId().getEmail());
	}
	
	@Test
	public void testK_reserveSeats_repeatingReserve() throws Exception{
		String seatHold = createdSeatGroupHoldId;
		String email = "test@test.com";
		try {
			String reserveId = ticketService.reserveSeats(seatHold, email);
			fail("Exception expected");
		}catch(Exception e) {
			assertEquals("Seat hold with id="+ seatHold + " already claimed", e.getMessage());
		}
	}

}
 