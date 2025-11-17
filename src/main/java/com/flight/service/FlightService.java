package com.flight.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flight.entity.Flight;
import com.flight.exception.ResourceNotFoundException;
import com.flight.repository.FlightRepository;
import com.flight.request.SearchReq;

@Service
public class FlightService {
	@Autowired
	FlightRepository flightRepo;

	public Flight addService(Flight flight) {

		return flightRepo.save(flight);
	}

	public List<Flight> searchService(SearchReq searchReq) throws ResourceNotFoundException {
		String from = searchReq.origin;
		String to = searchReq.destination;

		List<Flight> flights = flightRepo.findByOriginAndDestination(from, to);

		if (flights.isEmpty()) {
			throw new ResourceNotFoundException("No flights found from " + from + " to " + to);
		}

		return flights;
	}

	public String deleteFlightService(int flightId) throws ResourceNotFoundException {
		Flight flight = flightRepo.findById(flightId)
				.orElseThrow(() -> new ResourceNotFoundException("Flight with id " + flightId + " not found"));

		flightRepo.delete(flight);
		return "Flight with id " + flightId + " deleted successfully";
	}

}
