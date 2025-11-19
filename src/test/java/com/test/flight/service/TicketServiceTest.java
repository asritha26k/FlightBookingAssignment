package com.test.flight.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.flight.entity.Flight;
import com.flight.entity.Passenger;
import com.flight.entity.Status;
import com.flight.entity.Ticket;
import com.flight.exception.ResourceNotFoundException;
import com.flight.exception.ResourceNotFoundExceptionForResponseEntity;
import com.flight.repository.FlightRepository;
import com.flight.repository.PassengerRepository;
import com.flight.repository.TicketRepository;
import com.flight.request.TicketBookingRequest;
import com.flight.service.TicketService;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

	@Mock
	private TicketRepository ticketRepo;

	@Mock
	private PassengerRepository passRepo;

	@Mock
	private FlightRepository flightRepo;

	@InjectMocks
	private TicketService ticketService;

	private Passenger passenger;
	private Flight flight;
	private TicketBookingRequest request;

	@BeforeEach
	void setUp() {
		passenger = new Passenger();
		passenger.setPassengerId(1);
		passenger.setTicket(new ArrayList<>());

		flight = new Flight();
		flight.setFlightId(100);

		request = new TicketBookingRequest();
		request.passenger_id = 1;
		request.seatNo = "A1";
	}

	@Test
	void testBookTicketService_Success() throws ResourceNotFoundException {
		when(passRepo.findById(1)).thenReturn(Optional.of(passenger));
		when(flightRepo.findById(100)).thenReturn(Optional.of(flight));
		when(ticketRepo.save(any(Ticket.class))).thenAnswer(i -> i.getArguments()[0]);

		ResponseEntity<String> pnr = ticketService.bookTicketService(100, request);

		assertNotNull(pnr.getBody());
		assertEquals(1, passenger.getTicket().size());
		assertEquals(Status.Booked, passenger.getTicket().get(0).getStatus());
		verify(ticketRepo, times(1)).save(any(Ticket.class));
	}

	@Test
	void testBookTicketService_PassengerNotFound() {
		when(passRepo.findById(1)).thenReturn(Optional.empty());

		ResourceNotFoundExceptionForResponseEntity exception = assertThrows(
				ResourceNotFoundExceptionForResponseEntity.class, () -> ticketService.bookTicketService(100, request));

		assertEquals("Passenger with id 1 not found", exception.getMessage());
	}

	@Test
	void testBookTicketService_FlightNotFound() {
		when(passRepo.findById(1)).thenReturn(Optional.of(passenger));
		when(flightRepo.findById(100)).thenReturn(Optional.empty());

		ResourceNotFoundExceptionForResponseEntity exception = assertThrows(
				ResourceNotFoundExceptionForResponseEntity.class, () -> ticketService.bookTicketService(100, request));

		assertEquals("Flight with id 100 not found", exception.getMessage());
	}

	@Test
	void testGetServiceDetails() throws ResourceNotFoundException {
		Ticket ticket = new Ticket();
		ticket.setPnr("PNR12345");

		when(ticketRepo.findByPnr("PNR12345")).thenReturn(Optional.of(ticket));

		ResponseEntity<Ticket> result = ticketService.getServiceDetails("PNR12345");

		assertNotNull(result);
		assertEquals("PNR12345", result.getBody().getPnr());
	}

	@Test
	void testGetServiceDetails_NotFound() throws ResourceNotFoundExceptionForResponseEntity {
		when(ticketRepo.findByPnr("MISSING")).thenReturn(Optional.empty());

		ResourceNotFoundExceptionForResponseEntity exception = assertThrows(
				ResourceNotFoundExceptionForResponseEntity.class, () -> ticketService.getServiceDetails("MISSING"));
		assertEquals(exception.getMessage(), "MISSING " + "this pnr details not found");
	}

	@Test
	void testGetDelete_Success() throws ResourceNotFoundException {
		Passenger localPassenger = new Passenger();
		localPassenger.setPassengerId(1);
		localPassenger.setTicket(new ArrayList<>());

		Ticket ticket = new Ticket();
		ticket.setPnr("PNR12345");
		ticket.setPassenger(localPassenger);
		ticket.setStatus(Status.Booked);
		ticket.setSeatNo("A1");

		localPassenger.getTicket().add(ticket);

		when(ticketRepo.findByPnr("PNR12345")).thenReturn(Optional.of(ticket));

		ResponseEntity<String> result = ticketService.getDelete("PNR12345");

		assertEquals("Deleted PNR12345", result.getBody());
		assertTrue(localPassenger.getTicket().isEmpty());
		verify(ticketRepo, times(1)).delete(ticket);

	}

	@Test
	void testGetDelete_TicketNotFound() {
		String p = "PNR12345";
		when(ticketRepo.findByPnr(p)).thenReturn(Optional.empty());

		ResourceNotFoundExceptionForResponseEntity exception = assertThrows(
				ResourceNotFoundExceptionForResponseEntity.class, () -> ticketService.getDelete("PNR12345"));

		assertEquals(p + "this pnr details not found", exception.getMessage());
	}
}
