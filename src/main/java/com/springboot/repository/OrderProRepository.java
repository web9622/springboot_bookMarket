package com.springboot.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.springboot.domain.Order;

@Repository
public interface OrderProRepository extends JpaRepository<Order, Long> {
	
	   
	
	
}
