package com.assessment.inventory_and_order_management_system.Exception;

import org.springframework.http.HttpStatus;

public class InvalidRequestException extends ApiException {

	public InvalidRequestException(String message) {
		super(HttpStatus.BAD_REQUEST, message);
	}
}