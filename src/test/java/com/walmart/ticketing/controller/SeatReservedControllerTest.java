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
import com.walmart.ticketing.service.TicketServiceImpl;
import com.walmart.ticketing.service.VenueService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes=WalmartTicketingSystemApplication.class)
public class SeatReservedControllerTest {

	private MockMvc mockMvc;

	@Mock
	private TicketServiceImpl ticketService;
	
	@Mock
	private VenueService venueService;
	
	@InjectMocks
    private SeatReservedController seatReservedController;

	@Before
	public void setUp() throws Exception{
		this.mockMvc = MockMvcBuilders.standaloneSetup(seatReservedController).build();
		when(ticketService.reserveSeats("nonExistentHold", "test@test.com")).thenThrow(new Exception("There is no seat hold with id=nonExistentHold"));
		when(ticketService.reserveSeats("expiredHold", "test@test.com")).thenThrow(new Exception("Hold already expired"));
		String wrongEmail = "wrong@email.com";
		when(ticketService.reserveSeats("1", wrongEmail)).thenThrow(new Exception(wrongEmail + " cannot claim this hold. Expected customer email=test@test.com"));
		when(ticketService.reserveSeats("1", "test@test.com")).thenReturn("100");
		when(ticketService.reserveSeats("2", "test@test.com")).thenThrow(new Exception("Seat hold with id=2 already claimed"));
	}

	@Test
	public void testReserveSeats_noExistHold() throws Exception{
		mockMvc.perform(post("/api/seatReserves")
				.contentType(MediaType.APPLICATION_JSON)
				.header("host", "localhost")
				.param("email", "test@test.com")
				.param("seatHoldId", "nonExistentHold"))
				.andExpect(status().isOk())
				.andExpect(content().json("{\n" + 
						"    \"status\": \"FAILURE\",\n" + 
						"    \"message\": \"There is no seat hold with id=nonExistentHold\",\n" + 
						"    \"link\": null\n" + 
						"}"))
				.andReturn();
	}
	
	@Test
	public void testReserveSeats_expiredHold() throws Exception{	
		mockMvc.perform(post("/api/seatReserves")
				.contentType(MediaType.APPLICATION_JSON)
				.header("host", "localhost")
				.param("email", "test@test.com")
				.param("seatHoldId", "expiredHold"))
				.andExpect(status().isOk())
				.andExpect(content().json("{\n" + 
						"    \"status\": \"FAILURE\",\n" + 
						"    \"message\": \"Hold already expired\",\n" + 
						"    \"link\": null\n" + 
						"}"))
				.andReturn();
	}
	
	@Test
	public void testReserveSeats_wrongEmail() throws Exception{
		
		mockMvc.perform(post("/api/seatReserves")
				.contentType(MediaType.APPLICATION_JSON)
				.header("host", "localhost")
				.param("email", "wrong@email.com")
				.param("seatHoldId", "1"))
				.andExpect(status().isOk())
				.andExpect(content().json("{\n" + 
						"    \"status\": \"FAILURE\",\n" + 
						"    \"message\": \"wrong@email.com cannot claim this hold. Expected customer email=test@test.com\",\n" + 
						"    \"link\": null\n" + 
						"}"))
				.andReturn();
	}
	
	@Test
	public void testReserveSeats_success() throws Exception{
		
		mockMvc.perform(post("/api/seatReserves")
				.contentType(MediaType.APPLICATION_JSON)
				.header("host", "localhost")
				.param("email", "test@test.com")
				.param("seatHoldId", "1"))
				.andExpect(status().isOk())
				.andExpect(content().json("{\n" + 
						"    \"status\": \"SUCCESS\",\n" + 
						"    \"message\": \"Reserved with id=100\",\n" + 
						"    \"link\": \"http://localhost:80/api/seatReserves/100\"\n" + 
						"}"))
				.andReturn();
	}	
	
	@Test
	public void testReserveSeats_success2() throws Exception{
		
		mockMvc.perform(post("/api/seatReserves")
				.contentType(MediaType.APPLICATION_JSON)
				.header("host", "localhost")
				.param("email", "test@test.com")
				.param("seatHoldId", "2"))
				.andExpect(status().isOk())
				.andExpect(content().json("{\n" + 
						"    \"status\": \"FAILURE\",\n" + 
						"    \"message\": \"Seat hold with id=2 already claimed\",\n" + 
						"    \"link\": null" + 
						"}"))
				.andReturn();
	}	
}
