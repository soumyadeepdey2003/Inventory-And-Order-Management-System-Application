package com.assessment.inventory_and_order_management_system.Exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {

	public ResourceNotFoundException(String message) {
		super(HttpStatus.NOT_FOUND, message);
	}

	public static ResourceNotFoundException forId(String resourceName, Long id) {
		return new ResourceNotFoundException(resourceName + " not found with id: " + id);
	}

	public static ResourceNotFoundException forField(String resourceName, String fieldName, String fieldValue) {
		return new ResourceNotFoundException(resourceName + " not found with " + fieldName + ": " + fieldValue);
	}
}