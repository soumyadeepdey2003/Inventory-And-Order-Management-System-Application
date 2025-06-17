package com.assessment.inventory_and_order_management_system.Dto.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
	private int status;
	private String message;
	private String path;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime timestamp;

	@Builder.Default
	private List<ValidationError> errors = new ArrayList<>();

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ValidationError {
		private String field;
		private String message;
	}

	public void addValidationError(String field, String message) {
		if (errors == null) {
			errors = new ArrayList<>();
		}
		errors.add(new ValidationError(field, message));
	}
}