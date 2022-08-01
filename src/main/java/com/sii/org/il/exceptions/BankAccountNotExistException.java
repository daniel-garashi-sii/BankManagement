package com.sii.org.il.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = BankAccountNotExistException.BANK_ACCOUNT_NOT_EXIST_EXCEPTION)
public class BankAccountNotExistException extends RuntimeException {
	public static final String BANK_ACCOUNT_NOT_EXIST_EXCEPTION = "Bank account does not exist";
}