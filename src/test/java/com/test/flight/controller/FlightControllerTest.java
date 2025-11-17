package com.test.flight.controller;

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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flight.controller.FlightController;
import com.flight.entity.Airline;
import com.flight.entity.Flight;
import com.flight.request.SearchReq;
import com.flight.service.FlightService;

@ExtendWith(MockitoExtension.class)
class FlightControllerTest {

	private MockMvc mockMvc;

	@Mock
	private FlightService flightService;

	@InjectMocks
	private FlightController flightController;

	private ObjectMapper mapper;

	@BeforeEach
	void setup() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mockMvc = MockMvcBuilders.standaloneSetup(flightController).build();
	}

	private Flight createFlight() {
		Flight f = new Flight();
		f.setFlightId(1);
		f.setAirline(Airline.Indigo);
		f.setOrigin("Bengaluru");
		f.setDestination("Mumbai");
		f.setPrice(2500.0);
		f.setDepartureTime(LocalDateTime.of(2025, 12, 1, 10, 30));
		f.setArrivalTime(LocalDateTime.of(2025, 12, 1, 12, 0));
		return f;
	}

	@Test
	void addFlight_Success() throws Exception {
		Flight request = createFlight();
		request.setFlightId(0);
		Flight saved = createFlight();

		Mockito.when(flightService.addService(Mockito.any(Flight.class))).thenReturn(saved);

		mockMvc.perform(post("/api/v1.0/flight/airline/inventory/add").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request))).andExpect(status().isOk())
				.andExpect(jsonPath("$.flightId").value(1)).andExpect(jsonPath("$.origin").value("Bengaluru"))
				.andExpect(jsonPath("$.destination").value("Mumbai"));
	}

	@Test
	void searchFlight_Success() throws Exception {
		SearchReq req = new SearchReq();
		req.origin = "Bengaluru";
		req.destination = "Mumbai";

		Mockito.when(flightService.searchService(Mockito.any(SearchReq.class))).thenReturn(List.of(createFlight()));

		mockMvc.perform(post("/api/v1.0/flight/search").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(req))).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].origin").value("Bengaluru"))
				.andExpect(jsonPath("$[0].destination").value("Mumbai"));
	}

}
