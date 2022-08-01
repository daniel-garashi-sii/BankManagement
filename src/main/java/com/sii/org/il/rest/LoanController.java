package com.sii.org.il.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sii.org.il.entities.Loan;
import com.sii.org.il.exceptions.BankAccountNotExistException;
import com.sii.org.il.exceptions.BankAccountStausNotActiveException;
import com.sii.org.il.exceptions.CustomerNotExistException;
import com.sii.org.il.exceptions.CustomerStatusNotActiveException;
import com.sii.org.il.exceptions.IncorrectAssociationException;
import com.sii.org.il.exceptions.LoanAlreadyPaidException;
import com.sii.org.il.exceptions.LoanAssociatedException;
import com.sii.org.il.services.LoanService;

@RestController
@RequestMapping("api/loan-services")
public class LoanController {

	@Autowired
	private LoanService loanService;

	@RequestMapping(value = "loans", method = RequestMethod.GET)
	public ResponseEntity<List<Loan>> getLoans() {
		try {
			return new ResponseEntity<>(loanService.getLoans(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "{id}/{accountNumber}/taking-loan", method = RequestMethod.POST)
	public ResponseEntity<Loan> takingLoan(@PathVariable Long id, @PathVariable Long accountNumber,
			@RequestBody Loan loan) {
		try {
			return new ResponseEntity<>(loanService.saveLoan(id, accountNumber, loan), HttpStatus.OK);
		} catch (CustomerNotExistException e) {
			throw e;
		} catch (CustomerStatusNotActiveException e) {
			throw e;
		} catch (LoanAssociatedException e) {
			throw e;
		} catch (BankAccountNotExistException e) {
			throw e;
		} catch (BankAccountStausNotActiveException e) {
			throw e;
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "{id}/{loanNumber}/loan-repayment", method = RequestMethod.PUT)
	public ResponseEntity<Void> loanRepayment(@PathVariable Long id, @PathVariable Long loanNumber,
			@RequestParam Double partialPayment) {
		try {
			loanService.loanRepayment(id, loanNumber, partialPayment);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (CustomerNotExistException e) {
			throw e;
		} catch (CustomerStatusNotActiveException e) {
			throw e;
		} catch (IncorrectAssociationException e) {
			throw e;
		} catch (LoanAlreadyPaidException e) {
			throw e;
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
