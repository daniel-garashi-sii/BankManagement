package com.sii.org.il.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = LoanAssociatedException.LOAN_ASSOCIATED_EXCEPTION)
public class LoanAssociatedException extends RuntimeException {
	public static final String LOAN_ASSOCIATED_EXCEPTION = "It is not possible to take a loan for an account that is not associated with you";
}