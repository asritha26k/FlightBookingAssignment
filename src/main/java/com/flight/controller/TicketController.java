package com.flight.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.flight.entity.Ticket;
import com.flight.exception.ResourceNotFoundException;
import com.flight.request.TicketBookingRequest;
import com.flight.service.TicketService;

import jakarta.validation.Valid;

@RestController
public class TicketController {

	@Autowired
	TicketService ticketService;

	@PostMapping("/api/v1.0/flight/booking/{f_id}")
	public String bookTicket(@PathVariable int f_id, @Valid @RequestBody TicketBookingRequest req)
			throws ResourceNotFoundException {
		return ticketService.bookTicketService(f_id, req);

	}

	@GetMapping("/api/v1.0/flight/ticket/{pnr}")
	public Ticket getDetails(@PathVariable String pnr) {
		return ticketService.getServiceDetails(pnr);
	}

}
