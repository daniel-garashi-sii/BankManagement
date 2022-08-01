package com.sii.org.il.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sii.org.il.entities.Customer;
import com.sii.org.il.exceptions.CustomerAlreadyExistException;
import com.sii.org.il.exceptions.CustomerNotExistException;
import com.sii.org.il.services.CustomerService;

@RestController
@RequestMapping("api/customer-services")
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	
	@RequestMapping(value = "customers", method = RequestMethod.GET)
	public ResponseEntity<List<Customer>> getCustomers() {
		try {
			return new ResponseEntity<>(customerService.getCutomrs(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "customer/{id}", method = RequestMethod.GET)
	public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
		try {
			return new ResponseEntity<>(customerService.getCustomerById(id), HttpStatus.OK);
		} catch (CustomerNotExistException e) {
			throw e;
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "new-customer", method = RequestMethod.POST)
	public ResponseEntity<Customer> newCustomer(@RequestBody Customer customer) {
		try {
			return new ResponseEntity<>(customerService.saveCustomer(customer), HttpStatus.OK);
		} catch (CustomerAlreadyExistException e) {
			throw e;
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "deactivate-customer/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deactivateCustomer(@PathVariable Long id) {
		try {
			customerService.deactivateCustomer(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (CustomerNotExistException e) {
			throw e;
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "activate-customer/{id}", method = RequestMethod.POST)
	public ResponseEntity<Void> activateCustomer(@PathVariable Long id) {
		try {
			customerService.activateCustomer(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (CustomerNotExistException e) {
			throw e;
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "customer-details", method = RequestMethod.PUT)
	public ResponseEntity<Void> updateCustomerDetails(@RequestBody Customer customer) {
		try {
			customerService.updateCustomerDetails(customer);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
