package com.sii.org.il.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sii.org.il.entities.ForeignExchange;
import com.sii.org.il.exceptions.BankAccountNotExistException;
import com.sii.org.il.exceptions.CreditOverdraftException;
import com.sii.org.il.services.ForeignExchangeService;

@RestController
@RequestMapping("api/foreign-exchange-services")
public class ForeignExchangeController {

	@Autowired
	private ForeignExchangeService exchangeService;

	@RequestMapping(value = "foreign-currencies", method = RequestMethod.GET)
	public ResponseEntity<ForeignExchange> getForeignCurrencies() {
		try {
			return new ResponseEntity<>(exchangeService.getForeignCurrencies(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "deposit/{accountNumber}", method = RequestMethod.PUT)
	public ResponseEntity<Void> deposit(@PathVariable Long accountNumber, @RequestParam String from,
			@RequestParam Double amountToDeposit) {
		try {
			exchangeService.deposit(accountNumber, from, amountToDeposit);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (BankAccountNotExistException e) {
			throw e;
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "withdrawal/{accountNumber}", method = RequestMethod.PUT)
	public ResponseEntity<Void> withdrawal(@PathVariable Long accountNumber, @RequestParam String to,
			@RequestParam Double amountToWithdrawal) {
		try {
			exchangeService.withdrawal(accountNumber, to, amountToWithdrawal);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (BankAccountNotExistException e) {
			throw e;
		} catch (CreditOverdraftException e) {
			throw e;
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
