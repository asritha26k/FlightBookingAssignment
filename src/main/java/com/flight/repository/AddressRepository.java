package com.flight.repository;

import org.springframework.data.repository.CrudRepository;

import com.flight.entity.Address;

public interface AddressRepository extends CrudRepository<Address, Integer> {

}
