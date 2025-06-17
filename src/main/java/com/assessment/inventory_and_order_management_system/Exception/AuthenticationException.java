package com.assessment.inventory_and_order_management_system.Exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends ApiException {

	public AuthenticationException(String message) {
		super(HttpStatus.UNAUTHORIZED, message);
	}

	public static AuthenticationException invalidCredentials() {
		return new AuthenticationException("Invalid username or password");
	}

	public static AuthenticationException tokenExpired() {
		return new AuthenticationException("Authentication token has expired");
	}

	public static AuthenticationException invalidToken() {
		return new AuthenticationException("Invalid authentication token");
	}
}