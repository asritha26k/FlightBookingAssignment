package com.test.flight.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
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
import org.springframework.http.ResponseEntity;

import com.flight.entity.Address;
import com.flight.entity.Passenger;
import com.flight.entity.Ticket;
import com.flight.exception.ResourceNotFoundExceptionForResponseEntity;
import com.flight.repository.AddressRepository;
import com.flight.repository.PassengerRepository;
import com.flight.repository.TicketRepository;
import com.flight.request.PassengerRequest;
import com.flight.service.PassengerService;

public class PassengerServiceTest {

	@Mock
	private PassengerRepository passRepo;

	@Mock
	private AddressRepository addRepo;

	@Mock
	private TicketRepository tickRepo;

	@InjectMocks
	private PassengerService passengerService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testAddPassenger() {
		PassengerRequest req = new PassengerRequest();
		req.name = "Asritha";
		req.phoneNum = (long) 9876543;
		req.emailId = "asritha@example.com";
		req.city = "Bangalore";
		req.houseNo = 123;
		req.state = "KA";
		req.country = "India";

		Passenger passenger = new Passenger();
		passenger.setName(req.name);
		passenger.setPhoneNo(req.phoneNum);
		passenger.setEmailId(req.emailId);

		Address address = new Address();
		address.setCity(req.city);
		address.setHouseNo(req.houseNo);
		address.setState(req.state);
		address.setCountry(req.country);
		address.setPassenger(passenger);
		passenger.setAddress(address);

		when(passRepo.save(any(Passenger.class))).thenReturn(passenger);

		ResponseEntity<Passenger> savedPassenger = passengerService.add(req);

		assertEquals(req.name, savedPassenger.getBody().getName());
		assertEquals(req.emailId, savedPassenger.getBody().getEmailId());
		assertEquals(req.city, savedPassenger.getBody().getAddress().getCity());

		verify(passRepo, times(1)).save(any(Passenger.class));
	}

	@Test
	void testGetTicketsFound() throws ResourceNotFoundExceptionForResponseEntity {
		String email = "asritha@example.com";

		Passenger passenger = new Passenger();
		passenger.setPassengerId(1);
		passenger.setEmailId(email);

		Ticket ticket1 = new Ticket();
		Ticket ticket2 = new Ticket();
		List<Ticket> tickets = new ArrayList<>();
		tickets.add(ticket1);
		tickets.add(ticket2);

		when(passRepo.findByEmailId(email)).thenReturn(passenger);
		when(tickRepo.findAllByPassenger_PassengerId(1)).thenReturn(tickets);

		ResponseEntity<List<Ticket>> result = passengerService.getTickets(email);

		assertEquals(2, result.getBody().size());
		verify(passRepo, times(1)).findByEmailId(email);
		verify(tickRepo, times(1)).findAllByPassenger_PassengerId(1);
	}

	@Test
	void testGetTicketsNotFound() {
		String email = "unknown@example.com";

		when(passRepo.findByEmailId(email)).thenReturn(null);

		ResourceNotFoundExceptionForResponseEntity exception = assertThrows(
				ResourceNotFoundExceptionForResponseEntity.class, () -> {
					passengerService.getTickets(email);
				});

		assertEquals("Passenger with email unknown@example.com not found", exception.getMessage());
		verify(passRepo, times(1)).findByEmailId(email);
		verify(tickRepo, never()).findAllByPassenger_PassengerId(anyInt());
	}
}
