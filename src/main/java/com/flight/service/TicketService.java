package com.flight.service;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.flight.entity.Flight;
import com.flight.entity.Passenger;
import com.flight.entity.Status;
import com.flight.entity.Ticket;
import com.flight.exception.ResourceNotFoundExceptionForResponseEntity;
import com.flight.repository.FlightRepository;
import com.flight.repository.PassengerRepository;
import com.flight.repository.TicketRepository;
import com.flight.request.TicketBookingRequest;

@Service
public class TicketService {
	@Autowired
	TicketRepository ticketRepo;

	@Autowired
	PassengerRepository passRepo;

	@Autowired
	FlightRepository flightRepo;

	public ResponseEntity<String> bookTicketService(int flight_id, TicketBookingRequest req)
			throws ResourceNotFoundExceptionForResponseEntity {
		Passenger passenger = passRepo.findById(req.passenger_id)
				.orElseThrow(() -> new ResourceNotFoundExceptionForResponseEntity(
						"Passenger with id " + req.passenger_id + " not found"));

		Flight flight = flightRepo.findById(flight_id).orElseThrow(
				() -> new ResourceNotFoundExceptionForResponseEntity("Flight with id " + flight_id + " not found"));

		Ticket newTicket = new Ticket();
		newTicket.setFlight(flight);
		newTicket.setPassenger(passenger);
		newTicket.setSeatNo(req.seatNo);
		newTicket.setStatus(Status.Booked);

		String pnr = UUID.randomUUID().toString().substring(0, 8);
		newTicket.setPnr(pnr);

		if (passenger.getTicket() == null) {
			passenger.setTicket(new ArrayList<>());
		}

		passenger.getTicket().add(newTicket);
		ticketRepo.save(newTicket);

		return ResponseEntity.status(HttpStatus.CREATED).body(pnr);
	}

	public ResponseEntity<Ticket> getServiceDetails(String pnr) throws ResourceNotFoundExceptionForResponseEntity {

		Ticket ticket = ticketRepo.findByPnr(pnr)
				.orElseThrow(() -> new ResourceNotFoundExceptionForResponseEntity(pnr + " this pnr details not found"));

		return ResponseEntity.ok(ticket);
	}

	public ResponseEntity<String> getDelete(String pnr) throws ResourceNotFoundExceptionForResponseEntity {
		Ticket ticket = ticketRepo.findByPnr(pnr)
				.orElseThrow(() -> new ResourceNotFoundExceptionForResponseEntity(pnr + "this pnr details not found"));
		;

		Passenger passenger = ticket.getPassenger();
		if (passenger.getTicket() != null) {
			passenger.getTicket().remove(ticket);
		}
		ticketRepo.delete(ticket);
		return ResponseEntity.status(HttpStatus.OK).body("Deleted " + pnr);

	}
}
