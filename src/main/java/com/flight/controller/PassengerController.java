package com.flight.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.flight.entity.Passenger;
import com.flight.entity.Ticket;
import com.flight.exception.ResourceNotFoundExceptionForResponseEntity;
import com.flight.request.PassengerRequest;
import com.flight.service.PassengerService;
import com.flight.service.TicketService;

import jakarta.validation.Valid;

@RestController
public class PassengerController {
	@Autowired
	PassengerService passengerService;

	@Autowired
	TicketService tickService;

	// registered the passenger
	@PostMapping("/api/v1.0/flight/register/passenger")
	public ResponseEntity<Passenger> addPassenger(@Valid @RequestBody PassengerRequest req) {
		return passengerService.add(req);

	}

	@GetMapping("/api/v1.0/flight/booking/history/{emailId}")
	public ResponseEntity<List<Ticket>> getTickets(@PathVariable String emailId)
			throws ResourceNotFoundExceptionForResponseEntity {
		return passengerService.getTickets(emailId);
	}

	@DeleteMapping("/api/v1.0/flight/booking/cancel/{pnr}")
	public ResponseEntity<String> getDeleted(@PathVariable String pnr)
			throws ResourceNotFoundExceptionForResponseEntity {
		return tickService.getDelete(pnr);
	}

}
