package com.walmart.ticketing.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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
import com.walmart.ticketing.model.Venue;
import com.walmart.ticketing.model.output.FullVenueInfo;
import com.walmart.ticketing.service.TicketServiceImpl;
import com.walmart.ticketing.service.VenueService;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes=WalmartTicketingSystemApplication.class)
public class VenuesControllerTest {

	private MockMvc mockMvc;
	
	@Mock
    private VenueService venueService;
	
	@Mock
	private TicketServiceImpl ticketService;
	
	@InjectMocks
    private VenuesController venuesController;

	@Before
	public void setUp() throws Exception{
		this.mockMvc = MockMvcBuilders.standaloneSetup(venuesController).build();
		
		List<Venue> allVenues = new ArrayList<Venue>();
		
		Venue v1 = new Venue("BATMAN");
		v1.setId("1");
		Venue v2 = new Venue("SUPERMAN");
		v2.setId("2");
		allVenues.add(v1);
		allVenues.add(v2);
		
		FullVenueInfo vi = new FullVenueInfo();
		
		vi.setVenueId("1");
		vi.setVenueName("BATMAN");
		vi.setAvailableSeats(3);
		vi.setTotalSeats(3);
		vi.setAvailableSeatsDetails(new ArrayList<>());
		vi.setHoldSeatsDetails(new ArrayList<>());
		vi.setReservedSeatsDetails(new ArrayList<>());
		
		when(venueService.getAllVenues()).thenReturn(allVenues);
		when(venueService.getVenueInfo("1")).thenReturn(vi);
		when(ticketService.numSeatsAvailable("1")).thenReturn(vi.getAvailableSeats());
		when(ticketService.numSeatsAvailable("5000")).thenReturn(-1);
	}
	
	@Test
	public void testGetAllVenues() throws Exception {
		mockMvc.perform(get("/api/venues")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json("[{\"id\":\"1\",\"name\":\"BATMAN\"},{\"id\":\"2\",\"name\":\"SUPERMAN\"}]"))
				.andReturn();
	}
	
	@Test
	public void testGetVenueInfo_404() throws Exception{
		mockMvc.perform(get("/api/venues/5000")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError())
				.andReturn();
	}
		
	@Test
	public void testGetVenueInfo_202() throws Exception{
		mockMvc.perform(get("/api/venues/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json("{\n" + 
						"  \"availableSeats\": 3, \n" + 
						"  \"availableSeatsDetails\": [], \n" + 
						"  \"holdSeats\": 0, \n" + 
						"  \"holdSeatsDetails\": [], \n" + 
						"  \"reservedSeats\": 0, \n" + 
						"  \"reservedSeatsDetails\": [], \n" + 
						"  \"totalSeats\": 3, \n" + 
						"  \"venueId\": \"1\", \n" + 
						"  \"venueName\": \"BATMAN\"\n" + 
						"}"))
				.andReturn();
	}
	
	@Test
	public void testGetVenueAvailableSeats_404() throws Exception{
		mockMvc.perform(get("/api/venues/5000/availableSeats")
				.contentType(MediaType.TEXT_PLAIN))
				.andExpect(status().is4xxClientError())
				.andReturn();
	}
		
	@Test
	public void testGetVenueAvailableSeats_200() throws Exception{
		mockMvc.perform(get("/api/venues/1/availableSeats")
				.contentType(MediaType.TEXT_PLAIN))
				.andExpect(status().isOk())
				.andExpect(content().string("3"))
				.andReturn();
	}


}
