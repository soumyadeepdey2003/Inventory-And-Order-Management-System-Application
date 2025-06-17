package com.assessment.inventory_and_order_management_system.Exception;

import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends ApiException {

	public DuplicateResourceException(String message) {
		super(HttpStatus.CONFLICT, message);
	}

	public static DuplicateResourceException forField(String resourceName, String fieldName, String fieldValue) {
		return new DuplicateResourceException(resourceName + " with " + fieldName + " '" + fieldValue + "' already exists");
	}
}