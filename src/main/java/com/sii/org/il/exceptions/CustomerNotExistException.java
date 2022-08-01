package com.sii.org.il.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = CustomerNotExistException.CUSTOMER_NOT_EXIST_EXCEPTION)
public class CustomerNotExistException extends RuntimeException {
	public static final String CUSTOMER_NOT_EXIST_EXCEPTION = "Customer does not exist";
}