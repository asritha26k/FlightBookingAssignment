package com.flight.repository;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.flight.entity.Flight;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FlightRepository extends ReactiveCrudRepository<Flight, Integer> {
	Flux<Flight> findByOrigin(String origin);

	Flux<Flight> findByDestination(String destination);

	Flux<Flight> findByOriginAndDestination(String origin, String destination);

	Mono<Flight> findByflightId(int flightId);

}
