package com.springboot.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.springboot.domain.Address;
import com.springboot.domain.Customer;





public interface AddressRepository extends CrudRepository<Address, Long> {

	
	
}
