package com.assessment.inventory_and_order_management_system.Exception;

import org.springframework.http.HttpStatus;

public class JwtException extends ApiException {

	public JwtException(String message) {
		super(HttpStatus.UNAUTHORIZED, message);
	}

	public static JwtException invalidToken() {
		return new JwtException("Invalid JWT token");
	}

	public static JwtException expiredToken() {
		return new JwtException("JWT token has expired");
	}

	public static JwtException missingToken() {
		return new JwtException("JWT token is missing");
	}
}