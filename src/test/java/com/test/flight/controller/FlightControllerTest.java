package com.test.flight.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flight.controller.FlightController;
import com.flight.entity.Airline;
import com.flight.entity.Flight;
import com.flight.exception.ResourceNotFoundException;
import com.flight.request.SearchReq;
import com.flight.service.FlightService;

@ExtendWith(MockitoExtension.class)
class FlightControllerTest {

	private MockMvc mockMvc;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Mock
	private FlightService flightService;

	@InjectMocks
	private FlightController flightController;

	@BeforeEach
	void setUp() {
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mockMvc = MockMvcBuilders.standaloneSetup(flightController).build();
	}

	@Test
	void addFlight_Success() throws Exception {
		Flight requestFlight = new Flight();
		requestFlight.setAirline(Airline.Indigo);
		requestFlight.setOrigin("Bengaluru");
		requestFlight.setDestination("Mumbai");
		requestFlight.setPrice(2500.0);
		requestFlight.setDepartureTime(LocalDateTime.of(2025, 12, 1, 10, 30));
		requestFlight.setArrivalTime(LocalDateTime.of(2025, 12, 1, 12, 0));

		Flight savedFlight = new Flight();
		savedFlight.setFlightId(1);
		savedFlight.setAirline(requestFlight.getAirline());
		savedFlight.setOrigin(requestFlight.getOrigin());
		savedFlight.setDestination(requestFlight.getDestination());
		savedFlight.setPrice(requestFlight.getPrice());
		savedFlight.setDepartureTime(requestFlight.getDepartureTime());
		savedFlight.setArrivalTime(requestFlight.getArrivalTime());

		when(flightService.addService(org.mockito.ArgumentMatchers.any(Flight.class))).thenReturn(savedFlight);

		mockMvc.perform(post("/api/v1.0/flight/airline/inventory/add").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestFlight))).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andExpect(result -> {
					String json = result.getResponse().getContentAsString();
					assert json.contains("\"flightId\":1");
					assert json.contains("Bengaluru");
					assert json.contains("Mumbai");
				});
	}

	@Test
	void searchFlight_Success() throws Exception {
		SearchReq request = new SearchReq();
		request.origin = "Bengaluru";
		request.destination = "Mumbai";

		Flight flight = new Flight();
		flight.setFlightId(1);
		flight.setAirline(Airline.Indigo);
		flight.setOrigin("Bengaluru");
		flight.setDestination("Mumbai");
		flight.setPrice(2500.0);
		flight.setDepartureTime(LocalDateTime.of(2025, 12, 1, 10, 30));
		flight.setArrivalTime(LocalDateTime.of(2025, 12, 1, 12, 0));

		List<Flight> results = Arrays.asList(flight);

		when(flightService.searchService(org.mockito.ArgumentMatchers.any(SearchReq.class))).thenReturn(results);

		mockMvc.perform(post("/api/v1.0/flight/search").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andExpect(result -> {
					String json = result.getResponse().getContentAsString();
					assert json.contains("Bengaluru");
					assert json.contains("Mumbai");
				});
	}

	@Test
	void searchFlight_NotFound_Throws() throws Exception {
		SearchReq request = new SearchReq();
		request.origin = "Nowhere";
		request.destination = "Else";

		when(flightService.searchService(org.mockito.ArgumentMatchers.any(SearchReq.class)))
				.thenThrow(new ResourceNotFoundException("No flights found"));

		jakarta.servlet.ServletException thrown = org.junit.jupiter.api.Assertions.assertThrows(
				jakarta.servlet.ServletException.class,
				() -> mockMvc.perform(post("/api/v1.0/flight/search").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request))).andReturn());

		Throwable cause = thrown.getCause();
		assert cause instanceof ResourceNotFoundException;
		assert "No flights found".equals(cause.getMessage());
	}
}
