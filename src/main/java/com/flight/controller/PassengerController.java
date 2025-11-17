package com.flight.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.flight.entity.Passenger;
import com.flight.entity.Ticket;
import com.flight.exception.ResourceNotFoundException;
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
	public Passenger addPassenger(@Valid @RequestBody PassengerRequest req) {
		return passengerService.add(req);

	}

	@GetMapping("/api/v1.0/flight/booking/history/{emailId}")
	public List<Ticket> getTickets(@PathVariable String emailId) throws ResourceNotFoundException {
		return passengerService.getTickets(emailId);
	}

	@DeleteMapping("/api/v1.0/flight/booking/cancel/{pnr}")
	public String getDeleted(@PathVariable String pnr) throws ResourceNotFoundException {
		return tickService.getDelete(pnr);
	}

}
