package com.assessment.inventory_and_order_management_system.Exception;

import org.springframework.http.HttpStatus;

public class BusinessLogicException extends ApiException {

	public BusinessLogicException(String message) {
		super(HttpStatus.UNPROCESSABLE_ENTITY, message);
	}
}