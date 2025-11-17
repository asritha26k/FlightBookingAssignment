package com.test.flight.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.flight.entity.Flight;
import com.flight.exception.ResourceNotFoundException;
import com.flight.repository.FlightRepository;
import com.flight.request.SearchReq;
import com.flight.service.FlightService;

public class FlightServiceTest {

	@Mock
	private FlightRepository flightRepo;

	@InjectMocks
	private FlightService flightService; // note: matches the service class

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testAddService() {
		Flight flight = new Flight();
		flight.setFlightId(1);
		flight.setOrigin("DEL");
		flight.setDestination("BOM");

		when(flightRepo.save(flight)).thenReturn(flight);

		Flight saved = flightService.addService(flight);
		assertEquals(flight, saved);

		verify(flightRepo, times(1)).save(flight);
	}

	@Test
	void testSearchServiceFound() throws ResourceNotFoundException {
		SearchReq req = new SearchReq();
		req.origin = "DEL";
		req.destination = "BOM";

		List<Flight> flightList = new ArrayList<>();
		Flight flight = new Flight();
		flight.setFlightId(1);
		flight.setOrigin("DEL");
		flight.setDestination("BOM");
		flightList.add(flight);

		when(flightRepo.findByOriginAndDestination("DEL", "BOM")).thenReturn(flightList);

		List<Flight> result = flightService.searchService(req);

		assertEquals(1, result.size());
		assertEquals("DEL", result.get(0).getOrigin());
		assertEquals("BOM", result.get(0).getDestination());

		verify(flightRepo, times(1)).findByOriginAndDestination("DEL", "BOM");
	}

	@Test
	void testSearchServiceNotFound() {
		SearchReq req = new SearchReq();
		req.origin = "DEL";
		req.destination = "NYC";

		when(flightRepo.findByOriginAndDestination("DEL", "NYC")).thenReturn(new ArrayList<>());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
			flightService.searchService(req);
		});

		assertEquals("No flights found from DEL to NYC", exception.getMessage());
		verify(flightRepo, times(1)).findByOriginAndDestination("DEL", "NYC");
	}
}
