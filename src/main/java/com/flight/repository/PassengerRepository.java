package com.flight.repository;

import org.springframework.data.repository.CrudRepository;

import com.flight.entity.Passenger;

public interface PassengerRepository extends CrudRepository<Passenger, Integer> {

	Passenger findByEmailId(String emailId);

}
