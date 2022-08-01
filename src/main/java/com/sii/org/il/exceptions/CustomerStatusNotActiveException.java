package com.sii.org.il.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = CustomerStatusNotActiveException.CUSTOMER_STATUS_NOT_ACTIVE_EXCEPTION)
public class CustomerStatusNotActiveException extends RuntimeException {
	public static final String CUSTOMER_STATUS_NOT_ACTIVE_EXCEPTION = "customer must be active";
}
