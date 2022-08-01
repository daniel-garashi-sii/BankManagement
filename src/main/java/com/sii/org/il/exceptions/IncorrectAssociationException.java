package com.sii.org.il.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = IncorrectAssociationException.INCORRECT_ASSOCIATION_EXCEPTION)
public class IncorrectAssociationException extends RuntimeException {
	public static final String INCORRECT_ASSOCIATION_EXCEPTION = "Incorrect ID or Account number, please try again";
}