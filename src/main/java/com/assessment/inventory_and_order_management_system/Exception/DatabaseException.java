package com.assessment.inventory_and_order_management_system.Exception;

import org.springframework.http.HttpStatus;

public class DatabaseException extends ApiException {

	public DatabaseException(String message) {
		super(HttpStatus.INTERNAL_SERVER_ERROR, message);
	}

	public static DatabaseException operationFailed(String operation) {
		return new DatabaseException("Database operation failed: " + operation);
	}
}