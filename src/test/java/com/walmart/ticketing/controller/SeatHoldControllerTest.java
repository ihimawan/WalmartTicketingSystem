package com.walmart.ticketing.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.walmart.ticketing.WalmartTicketingSystemApplication;
import com.walmart.ticketing.model.SeatGroupHold;
import com.walmart.ticketing.service.SeatGroupHoldService;
import com.walmart.ticketing.service.TicketServiceImpl;
import com.walmart.ticketing.service.VenueService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes=WalmartTicketingSystemApplication.class)
public class SeatHoldControllerTest {

	private MockMvc mockMvc;
	
	@Mock
    private SeatGroupHoldService sghService;
	
	@Mock
	private TicketServiceImpl ticketService;
	
	@Mock
	private VenueService venueService;
	
	@InjectMocks
    private SeatHoldController seatHoldController;

	@Before
	public void setUp() throws Exception{
		this.mockMvc = MockMvcBuilders.standaloneSetup(seatHoldController).build();
		when(ticketService.findAndHoldSeats(10, "test@test.com", "nonExistentID")).thenThrow(new Exception("Venue with id=nonExistentID is not found."));
		when(ticketService.findAndHoldSeats(10000, "test@test.com", "existID")).thenThrow(new Exception("Unable to get best seats. There are only 20 available while " + 10000 + " is requested."));
		when(ticketService.findAndHoldSeats(0, "test@test.com", "existID")).thenThrow(new Exception("0 number of seats cannot be booked."));
		SeatGroupHold newSgh = new SeatGroupHold();
		newSgh.setId("123");
		when(ticketService.findAndHoldSeats(20, "test@test.com", "existID")).thenReturn(newSgh);
	}

	@Test
	public void testRequestHold_noExistVenue() throws Exception {
		mockMvc.perform(post("/api/seatHolds")
				.contentType(MediaType.APPLICATION_JSON)
				.header("host", "localhost")
				.param("email", "test@test.com")
				.param("numSeats", "10")
				.param("venueId", "nonExistentID"))
				.andExpect(status().isOk())
				.andExpect(content().json("{\n" + 
						"    \"status\": \"FAILURE\",\n" + 
						"    \"message\": \"Venue with id=nonExistentID is not found.\",\n" + 
						"    \"link\": null\n" + 
						"}"))
				.andReturn();
	}
	
	@Test
	public void testRequestHold_invalidNumOfSeats() throws Exception {
		mockMvc.perform(post("/api/seatHolds")
				.contentType(MediaType.APPLICATION_JSON)
				.header("host", "localhost")
				.param("email", "test@test.com")
				.param("numSeats", "0")
				.param("venueId", "existID"))
				.andExpect(status().isOk())
				.andExpect(content().json("{\n" + 
						"    \"status\": \"FAILURE\",\n" + 
						"    \"message\": \"0 number of seats cannot be booked.\",\n" + 
						"    \"link\": null\n" + 
						"}"))
				.andReturn();
	}
	
	@Test
	public void testRequestHold_tooBigRequest() throws Exception {
		
		mockMvc.perform(post("/api/seatHolds")
				.contentType(MediaType.APPLICATION_JSON)
				.header("host", "localhost")
				.param("email", "test@test.com")
				.param("numSeats", "10000")
				.param("venueId", "existID"))
				.andExpect(status().isOk())
				.andExpect(content().json("{\n" + 
						"    \"status\": \"FAILURE\",\n" + 
						"    \"message\": \"Unable to get best seats. There are only 20 available while 10000 is requested.\",\n" + 
						"    \"link\": null\n" + 
						"}"))
				.andReturn();
	}
		
	@Test
	public void testRequestHold_resultSuccess() throws Exception {
			
		mockMvc.perform(post("/api/seatHolds")
				.contentType(MediaType.APPLICATION_JSON)
				.header("host", "localhost")
				.param("email", "test@test.com")
				.param("numSeats", "20")
				.param("venueId", "existID"))
				.andExpect(status().isOk())
				.andExpect(content().json("{\n" + 
						"    \"status\": \"SUCCESS\",\n" + 
						"    \"message\": \"Your seatHoldId=123\",\n" + 
						"    \"link\": \"http://localhost:80/api/seatHolds/123\"\n" + 
						"}"))
				.andReturn();
	}

}
