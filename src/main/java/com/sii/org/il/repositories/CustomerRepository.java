package com.sii.org.il.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sii.org.il.entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	
}
