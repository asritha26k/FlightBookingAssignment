package com.flight.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.flight.entity.Address;

public interface AddressRepository extends ReactiveCrudRepository<Address, Integer> {

}
