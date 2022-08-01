package com.sii.org.il.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = BankAccountStausNotActiveException.BANK_ACCOUNT_STAUS_NOT_ACTIVE_EXCEPTION)
public class BankAccountStausNotActiveException extends RuntimeException {
	public static final String BANK_ACCOUNT_STAUS_NOT_ACTIVE_EXCEPTION = "Bank account must be active";
}
