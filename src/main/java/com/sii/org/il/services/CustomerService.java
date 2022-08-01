package com.sii.org.il.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sii.org.il.entities.Customer;
import com.sii.org.il.entities.enums.CustomerStatus;
import com.sii.org.il.exceptions.CustomerAlreadyExistException;
import com.sii.org.il.exceptions.CustomerNotExistException;
import com.sii.org.il.repositories.CustomerRepository;

@Service
public class CustomerService {
	@Autowired
	private CustomerRepository customerRepository;

	public List<Customer> getCutomrs() {
		return customerRepository.findAll();
	}

	public Customer getCustomerById(Long id) throws CustomerNotExistException {
		Optional<Customer> customer = customerRepository.findById(id);
		if (customer.isPresent())
			return customer.get();

		throw new CustomerNotExistException();
	}

	@Transactional
	public void deactivateCustomer(Long id) throws CustomerNotExistException {
		Customer customer = getCustomerById(id);
		customer.setStatus(CustomerStatus.DEACTIVE);
	}

	@Transactional
	public void activateCustomer(Long id) throws CustomerNotExistException {
		Customer customer = getCustomerById(id);
		customer.setStatus(CustomerStatus.ACTIVE);
	}

	@Transactional
	public Customer saveCustomer(Customer customer) throws CustomerAlreadyExistException {
		if(isCustomerExists(customer.getId()))
			throw new CustomerAlreadyExistException();

		return customerRepository.save(customer);
	}

	@Transactional
	public void updateCustomerDetails(Customer customer) {
		Customer c = customerRepository.getReferenceById(customer.getId());
		
		c.setFirstName(customer.getFirstName());
		c.setLastName(customer.getLastName());
		c.setEmail(customer.getEmail());
	}
	
	public boolean isCustomerExists(Long id) {
		return customerRepository.existsById(id);
	}

	public boolean isCustomerStatusNotActive(CustomerStatus status) {
		return CustomerStatus.ACTIVE != status;
	}

}
