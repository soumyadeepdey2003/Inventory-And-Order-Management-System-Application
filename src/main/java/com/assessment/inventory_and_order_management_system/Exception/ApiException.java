package com.assessment.inventory_and_order_management_system.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ApiException extends RuntimeException {
	private final HttpStatus status;
	private final String message;
	private final LocalDateTime timestamp;

	public ApiException(HttpStatus status, String message) {
		super(message);
		this.status = status;
		this.message = message;
		this.timestamp = LocalDateTime.now();
	}
}