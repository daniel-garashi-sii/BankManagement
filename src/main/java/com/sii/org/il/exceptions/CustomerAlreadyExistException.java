package com.sii.org.il.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = CustomerAlreadyExistException.CUSTOMER_ALREADY_EXIST_EXCEPTION)
public class CustomerAlreadyExistException extends RuntimeException {
	public static final String CUSTOMER_ALREADY_EXIST_EXCEPTION = "Customer already exists";
}

