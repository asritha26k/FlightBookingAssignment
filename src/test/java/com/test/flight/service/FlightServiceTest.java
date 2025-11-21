package com.test.flight.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.flight.entity.Airline;
import com.flight.entity.Flight;
import com.flight.exception.ResourceNotFoundException;
import com.flight.exception.ResourceNotFoundExceptionForResponseEntity;
import com.flight.repository.FlightRepository;
import com.flight.request.SearchReq;
import com.flight.service.FlightService;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

	@Mock
	FlightRepository flightRepo;

	@InjectMocks
	FlightService flightService;

	@Test
	public void testAddService() throws ResourceNotFoundException {
		Flight input = new Flight();
		input.setAirline(Airline.Emirates);
		input.setFlightId(1);
		input.setOrigin("india");
		input.setDestination("pakistan");
		when(flightRepo.save(input)).thenReturn(input);
		ResponseEntity<Integer> output = flightService.addService(input);
		assertEquals(output.getStatusCode(), HttpStatus.CREATED);
		verify(flightRepo, times(1)).save(input);
	}

	@Test
	public void testSearchService() throws ResourceNotFoundException {
		SearchReq searchReq = new SearchReq();
		searchReq.origin = "india";
		searchReq.destination = "pakistan";
		List<Flight> flights = new ArrayList<>();

		Flight input = new Flight();
		input.setAirline(Airline.Emirates);
		input.setFlightId(1);
		input.setOrigin("india");
		input.setDestination("pakistan");

		flights.add(input);
		when(flightRepo.findByOriginAndDestination(searchReq.origin, searchReq.destination)).thenReturn(flights);

		ResponseEntity<List<Flight>> output = flightService.searchService(searchReq);
		assertEquals(flights, output.getBody());
		verify(flightRepo, times(1)).findByOriginAndDestination(searchReq.origin, searchReq.destination);

	}

	@Test
	public void testFailedSearchService() throws ResourceNotFoundExceptionForResponseEntity {
		SearchReq searchReq = new SearchReq();
		searchReq.origin = "india";
		searchReq.destination = "pakistan";
		List<Flight> flights = new ArrayList<>();
		when(flightRepo.findByOriginAndDestination(searchReq.origin, searchReq.destination)).thenReturn(flights);
		// returning empty flight lists will throw error
		ResourceNotFoundExceptionForResponseEntity exception = assertThrows(
				ResourceNotFoundExceptionForResponseEntity.class, () -> flightService.searchService(searchReq));
		assertEquals("No flights found from " + searchReq.origin + " to " + searchReq.destination,
				exception.getMessage());

	}

	@Test
	public void Deletion() throws ResourceNotFoundException {
	    Flight input = new Flight();
	    input.setAirline(Airline.Emirates);
	    input.setFlightId(1);
	    input.setOrigin("india");
	    input.setDestination("pakistan");
	    when(flightRepo.findById(1)).thenReturn(Optional.of(input));
	    ResponseEntity<Void> response = flightService.deleteFlightService(1);
	    assertEquals(HttpStatus.OK, response.getStatusCode());
	    assertNull(response.getBody());
	    verify(flightRepo, times(1)).delete(input);
	}


	@Test
	public void DeletionFailed() throws ResourceNotFoundExceptionForResponseEntity {
		when(flightRepo.findById(1)).thenReturn(Optional.empty());
		ResourceNotFoundExceptionForResponseEntity exception = assertThrows(
				ResourceNotFoundExceptionForResponseEntity.class, () -> flightService.deleteFlightService(1));

	}

}
