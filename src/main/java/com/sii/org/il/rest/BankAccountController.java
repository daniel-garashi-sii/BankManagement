package com.sii.org.il.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sii.org.il.entities.BankAccount;
import com.sii.org.il.exceptions.BankAccountNotExistException;
import com.sii.org.il.exceptions.CreditOverdraftException;
import com.sii.org.il.exceptions.CustomerNotExistException;
import com.sii.org.il.exceptions.CustomerStatusNotActiveException;
import com.sii.org.il.exceptions.IncorrectAssociationException;
import com.sii.org.il.services.BankAccountService;

@RestController
@RequestMapping("api/bank-account-services")
public class BankAccountController {

	@Autowired
	BankAccountService bankAccountService;

	@RequestMapping(value = "bank-accounts", method = RequestMethod.GET)
	public ResponseEntity<List<BankAccount>> getBankAccounts() {
		try {
			return new ResponseEntity<>(bankAccountService.getBankAccounts(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "open-account/{id}", method = RequestMethod.POST)
	public ResponseEntity<BankAccount> openBankAccount(@PathVariable Long id) {
		try {
			return new ResponseEntity<>(bankAccountService.openBankAccount(id), HttpStatus.OK);
		} catch (CustomerNotExistException e) {
			throw e;
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "suspended-account/{accountNumebr}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> suspendedAccount(@PathVariable Long accountNumebr) {
		try {
			bankAccountService.suspendedAccount(accountNumebr);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (BankAccountNotExistException e) {
			throw e;
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "deposit/{accountNumebr}", method = RequestMethod.PUT)
	public ResponseEntity<Void> deposit(@PathVariable Long accountNumebr, @RequestParam Double depositAmount) {
		try {
			bankAccountService.deposit(accountNumebr, depositAmount);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (BankAccountNotExistException e) {
			throw e;
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "withdrawal/{accountNumebr}", method = RequestMethod.PUT)
	public ResponseEntity<Void> withdrawal(@PathVariable Long accountNumebr, @RequestParam Double withdrawalAmount) {
		try {
			bankAccountService.withdrawal(accountNumebr, withdrawalAmount);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (CreditOverdraftException e) {
			throw e;
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "{id}/balance/{accountNumebr}", method = RequestMethod.GET)
	public ResponseEntity<Double> getBalance(@PathVariable Long id, @PathVariable Long accountNumebr) {
		try {
			return new ResponseEntity<>(bankAccountService.getBalanceByBankAccountNumber(id, accountNumebr),
					HttpStatus.OK);
		} catch (CustomerNotExistException e) {
			throw e;
		} catch (IncorrectAssociationException e) {
			throw e;
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "{id}/transfer/from/{fromAccountNumber}/to/{toAccountNumber}", method = RequestMethod.PUT)
	public ResponseEntity<Void> transferBetweenBankAccounts(@PathVariable Long id, @PathVariable Long fromAccountNumber,
			@RequestParam Double transferAmount, @PathVariable Long toAccountNumber) {
		try {
			bankAccountService.transferBetweenBankAccounts(id, fromAccountNumber, transferAmount, toAccountNumber);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (CustomerNotExistException e) {
			throw e;
		} catch (CustomerStatusNotActiveException e) {
			throw e;
		} catch (IncorrectAssociationException e) {
			throw e;
		} catch (BankAccountNotExistException e) {
			throw e;
		} catch (CreditOverdraftException e) {
			throw e;
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
