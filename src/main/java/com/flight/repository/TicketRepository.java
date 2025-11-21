package com.flight.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.flight.entity.Ticket;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TicketRepository extends ReactiveCrudRepository<Ticket, Integer> {

	Mono<Ticket> findByPnr(String pnr);

	Flux<Ticket> findAllByPassengerId(Integer passengerId);
    

}
