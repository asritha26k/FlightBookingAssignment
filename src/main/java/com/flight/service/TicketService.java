package com.flight.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flight.entity.Flight;
import com.flight.entity.Passenger;
import com.flight.entity.Status;
import com.flight.entity.Ticket;
import com.flight.exception.ResourceNotFoundException;
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

	public String bookTick(int flight_id, TicketBookingRequest req) throws ResourceNotFoundException {
		Passenger passenger = passRepo.findById(req.passenger_id).orElseThrow(
				() -> new ResourceNotFoundException("Passenger with id " + req.passenger_id + " not found"));

		Flight flight = flightRepo.findById(flight_id)
				.orElseThrow(() -> new ResourceNotFoundException("Flight with id " + flight_id + " not found"));

		Ticket newTicket = new Ticket();
		newTicket.setFlight(flight);
		newTicket.setPassenger(passenger);
		newTicket.setSeatNo(req.seatNo);
		newTicket.setStatus(Status.Booked);

		String pnr = UUID.randomUUID().toString().substring(0, 8);
		newTicket.setPnr(pnr);

		passenger.getTicket().add(newTicket);
		ticketRepo.save(newTicket);

		return pnr;
	}

	public Ticket getServiceDetails(String pnr) {
		return ticketRepo.findByPnr(pnr);
	}

	public String getDelete(String pnr) throws ResourceNotFoundException {
		Ticket ticket = ticketRepo.findByPnr(pnr);
		if (ticket == null) {
			throw new ResourceNotFoundException("Ticket with PNR " + pnr + " not found");
		}

		Passenger passenger = ticket.getPassenger();
		passenger.getTicket().remove(ticket);
		ticketRepo.delete(ticket);

		return "Deleted " + pnr;
	}
}
