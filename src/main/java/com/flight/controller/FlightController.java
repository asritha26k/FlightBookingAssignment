package com.flight.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.flight.entity.Flight;
import com.flight.exception.ResourceNotFoundExceptionForResponseEntity;
import com.flight.request.SearchReq;
import com.flight.service.FlightService;

import jakarta.validation.Valid;

@RestController
public class FlightController {

	@Autowired
	FlightService flightService;

	// flight added
	@PostMapping("/api/v1.0/flight/airline/inventory/add")
	public ResponseEntity<Flight> addController(@Valid @RequestBody Flight flight) {
		return flightService.addService(flight);
	}

	@PostMapping("/api/v1.0/flight/search")
	public ResponseEntity<List<Flight>> searchController(@Valid @RequestBody SearchReq searchReq)
			throws ResourceNotFoundExceptionForResponseEntity {

		return flightService.searchService(searchReq);
	}

	@DeleteMapping("/api/v1.0/flight/airline/inventory/delete/{flightId}")
	public ResponseEntity<String> deleteFlightController(@PathVariable int flightId)
			throws ResourceNotFoundExceptionForResponseEntity {
//		if (flightService.getByFlightId(flightId).size() == 0) {
//			throw new ResourceNotFoundExceptionForResponseEntity("that flight id doesnt exist");
//		}
		return flightService.deleteFlightService(flightId);
	}

}
