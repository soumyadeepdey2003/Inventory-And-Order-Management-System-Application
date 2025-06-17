package com.assessment.inventory_and_order_management_system.Exception;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends ApiException {

	public AuthorizationException(String message) {
		super(HttpStatus.FORBIDDEN, message);
	}

	public static AuthorizationException insufficientPermissions() {
		return new AuthorizationException("You don't have permission to perform this action");
	}

	public static AuthorizationException forRole(String requiredRole) {
		return new AuthorizationException("This action requires " + requiredRole + " role");
	}
}