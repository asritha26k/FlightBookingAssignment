package com.test.flight.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flight.GlobalExceptionHandler;
import com.flight.controller.FlightController;
import com.flight.entity.Airline;
import com.flight.entity.Flight;
import com.flight.request.SearchReq;
import com.flight.service.FlightService;

@ExtendWith(MockitoExtension.class)
class FlightControllerTest {
	MockMvc mockMvc; // layer that stimulates controller
	@Mock
	FlightService flightService;
	@InjectMocks
	FlightController flightController;
	ObjectMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());// Because Jackson (ObjectMapper) cannot automatically serialize or
													// deserialize Java 8 date/time classes like:
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		/*
		 * It forces Jackson to use ISO-8601 string format, not numbers. So instead of:
		 * 1733049000 You get a readable date: "2025-12-01T10:30:00"
		 * 
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(flightController).setControllerAdvice(new GlobalExceptionHandler())
				.build();
	}

	public Flight createFlight() {
		Flight flight = new Flight();
		flight.setFlightId(1);
		flight.setOrigin("India");
		flight.setDestination("Pakistan");
		flight.setPrice(200);
		flight.setDepartureTime(LocalDateTime.of(2025, 12, 1, 10, 30));
		flight.setArrivalTime(LocalDateTime.of(2025, 12, 1, 12, 0));
		flight.setAirline(Airline.Emirates);
		return flight;
	}

	@Test
	public void addControllerTest() throws JsonProcessingException, Exception {
		Flight request = createFlight();
		request.setFlightId(0);
		Flight saved = createFlight();
		when(flightService.addService(Mockito.any(Flight.class)))
				.thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(saved));

		mockMvc.perform(post("/api/v1.0/flight/airline/inventory/add").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.flightId").value(1)).andExpect(jsonPath("$.origin").value("India"));
		// post,contentType,content one set
		// each andExpect is one set which contains json path and value check brackets!

	}

	@Test
	public void searchControllerTest() throws JsonProcessingException, Exception {
		Flight request = createFlight();
		List<Flight> flights = List.of(request);
		when(flightService.searchService(Mockito.any(SearchReq.class)))
				.thenReturn(ResponseEntity.status(HttpStatus.OK).body(flights));
		SearchReq req = new SearchReq();
		req.origin = "India";
		req.destination = "Pakistan";

		mockMvc.perform(post("/api/v1.0/flight/search").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(req))).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].flightId").value(1));

	}

	@Test
	public void deleteFlightControllerTest() throws Exception {

		when(flightService.deleteFlightService(1))
				.thenReturn(ResponseEntity.status(HttpStatus.OK).body("Flight with id " + 1 + " deleted successfully"));

		mockMvc.perform(delete("/api/v1.0/flight/airline/inventory/delete/{flightId}", 1)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$").value("Flight with id 1 deleted successfully"));
	}

}