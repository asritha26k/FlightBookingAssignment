package com.flight.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.flight.entity.Address;
import com.flight.entity.Passenger;
import com.flight.entity.Ticket;
import com.flight.exception.ResourceNotFoundExceptionForResponseEntity;
import com.flight.repository.AddressRepository;
import com.flight.repository.PassengerRepository;
import com.flight.repository.TicketRepository;
import com.flight.request.PassengerRequest;

@Service
public class PassengerService {
	@Autowired
	PassengerRepository passRepo;

	@Autowired
	AddressRepository addRepo;

	@Autowired
	TicketRepository tickRepo;

	public ResponseEntity<Passenger> add(PassengerRequest req) {
		Passenger newPassenger = new Passenger();
		newPassenger.setName(req.name);
		newPassenger.setPhoneNo(req.phoneNum);
		newPassenger.setEmailId(req.emailId);

		Address newAddress = new Address();
		newAddress.setCity(req.city);
		newAddress.setHouseNo(req.houseNo);
		newAddress.setCountry(req.country);
		newAddress.setState(req.state);

		newAddress.setPassenger(newPassenger);
		newPassenger.setAddress(newAddress);

		return ResponseEntity.status(HttpStatus.CREATED).body(passRepo.save(newPassenger));

	}

	public ResponseEntity<List<Ticket>> getTickets(String emailId) throws ResourceNotFoundExceptionForResponseEntity {
		Passenger p = passRepo.findByEmailId(emailId);

		if (p == null) {
			throw new ResourceNotFoundExceptionForResponseEntity("Passenger with email " + emailId + " not found");
		}

		return ResponseEntity.status(HttpStatus.OK).body(tickRepo.findAllByPassenger_PassengerId(p.getPassengerId()));
	}

}
