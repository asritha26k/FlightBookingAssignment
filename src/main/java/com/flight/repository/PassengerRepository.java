package com.flight.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.flight.entity.Passenger;

import reactor.core.publisher.Mono;

public interface PassengerRepository extends ReactiveCrudRepository<Passenger, Integer> {

	Mono<Passenger> findByEmailId(String emailId);

}
