package com.sii.org.il.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = CreditOverdraftException.CREDIT_OVERDRAFT_EXCEPTION)
public class CreditOverdraftException extends RuntimeException {
	public static final String CREDIT_OVERDRAFT_EXCEPTION = 
			"Exceeding in your bank account, Please try to withdrawal smaller amount or contact the bank's management";
	
}