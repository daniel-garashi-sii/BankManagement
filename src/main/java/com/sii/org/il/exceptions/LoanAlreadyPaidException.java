package com.sii.org.il.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = LoanAlreadyPaidException.LOAN_ALREADY_PAID_EXCEPTION)
public class LoanAlreadyPaidException extends RuntimeException {
	public static final String LOAN_ALREADY_PAID_EXCEPTION = "Loan already paid";
}