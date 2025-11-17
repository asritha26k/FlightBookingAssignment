package com.flight.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flight.entity.Address;
import com.flight.entity.Passenger;
import com.flight.entity.Ticket;
import com.flight.exception.ResourceNotFoundException;
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

	public Passenger add(PassengerRequest req) {
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

		return passRepo.save(newPassenger);

	}

	public List<Ticket> getTickets(String emailId) throws ResourceNotFoundException {
		Passenger p = passRepo.findByEmailId(emailId);

		if (p == null) {
			throw new ResourceNotFoundException("Passenger with email " + emailId + " not found");
		}

		return tickRepo.findAllByPassenger_PassengerId(p.getPassengerId());
	}

}
