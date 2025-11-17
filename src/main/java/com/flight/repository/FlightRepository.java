package com.flight.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flight.entity.Flight;

public interface FlightRepository extends CrudRepository<Flight, Integer> {
	List<Flight> findByOrigin(String origin);

	List<Flight> findByDestination(String destination);

	List<Flight> findByOriginAndDestination(String origin, String destination);

}
