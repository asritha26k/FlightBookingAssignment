package com.flight.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.flight.entity.Ticket;

public interface TicketRepository extends CrudRepository<Ticket, Integer> {

	Optional<Ticket> findByPnr(String pnr);

	List<Ticket> findAllByPassenger_PassengerId(int passengerId);

}
