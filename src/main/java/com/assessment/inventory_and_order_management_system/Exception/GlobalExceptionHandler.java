package com.assessment.inventory_and_order_management_system.Exception;

import com.assessment.inventory_and_order_management_system.Dto.Response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	/**
	 * Handle ApiException and its subclasses
	 */
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ErrorResponse> handleApiException(ApiException ex, WebRequest request) {
		log.error("API Exception: {}", ex.getMessage(), ex);

		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(ex.getStatus().value())
				.message(ex.getMessage())
				.path(getRequestPath(request))
				.timestamp(ex.getTimestamp())
				.build();

		return new ResponseEntity<>(errorResponse, ex.getStatus());
	}

	/**
	 * Handle Resource Not Found Exception
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		log.error("Resource Not Found Exception: {}", ex.getMessage(), ex);

		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.NOT_FOUND.value())
				.message(ex.getMessage())
				.path(getRequestPath(request))
				.timestamp(LocalDateTime.now())
				.build();

		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	/**
	 * Handle Duplicate Resource Exception
	 */
	@ExceptionHandler(DuplicateResourceException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<ErrorResponse> handleDuplicateResourceException(DuplicateResourceException ex, WebRequest request) {
		log.error("Duplicate Resource Exception: {}", ex.getMessage(), ex);

		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.CONFLICT.value())
				.message(ex.getMessage())
				.path(getRequestPath(request))
				.timestamp(LocalDateTime.now())
				.build();

		return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	}

	/**
	 * Handle Insufficient Inventory Exception
	 */
	@ExceptionHandler(InsufficientInventoryException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> handleInsufficientInventoryException(InsufficientInventoryException ex, WebRequest request) {
		log.error("Insufficient Inventory Exception: {}", ex.getMessage(), ex);

		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST.value())
				.message(ex.getMessage())
				.path(getRequestPath(request))
				.timestamp(LocalDateTime.now())
				.build();

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle Invalid Request Exception
	 */
	@ExceptionHandler(InvalidRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> handleInvalidRequestException(InvalidRequestException ex, WebRequest request) {
		log.error("Invalid Request Exception: {}", ex.getMessage(), ex);

		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST.value())
				.message(ex.getMessage())
				.path(getRequestPath(request))
				.timestamp(LocalDateTime.now())
				.build();

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle Authentication Exception
	 */
	@ExceptionHandler({
			AuthenticationException.class,
			BadCredentialsException.class
	})
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception ex, WebRequest request) {
		log.error("Authentication Exception: {}", ex.getMessage(), ex);

		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.UNAUTHORIZED.value())
				.message(ex.getMessage())
				.path(getRequestPath(request))
				.timestamp(LocalDateTime.now())
				.build();

		return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Handle Authorization Exception
	 */
	@ExceptionHandler({AuthorizationException.class, AccessDeniedException.class})
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<ErrorResponse> handleAuthorizationException(Exception ex, WebRequest request) {
		log.error("Authorization Exception: {}", ex.getMessage(), ex);

		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.FORBIDDEN.value())
				.message(ex.getMessage())
				.path(getRequestPath(request))
				.timestamp(LocalDateTime.now())
				.build();

		return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
	}

	/**
	 * Handle JWT Exception
	 */
	@ExceptionHandler(JwtException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ErrorResponse> handleJwtException(JwtException ex, WebRequest request) {
		log.error("JWT Exception: {}", ex.getMessage(), ex);

		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.UNAUTHORIZED.value())
				.message(ex.getMessage())
				.path(getRequestPath(request))
				.timestamp(LocalDateTime.now())
				.build();

		return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Handle Business Logic Exception
	 */
	@ExceptionHandler(BusinessLogicException.class)
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	public ResponseEntity<ErrorResponse> handleBusinessLogicException(BusinessLogicException ex, WebRequest request) {
		log.error("Business Logic Exception: {}", ex.getMessage(), ex);

		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.UNPROCESSABLE_ENTITY.value())
				.message(ex.getMessage())
				.path(getRequestPath(request))
				.timestamp(LocalDateTime.now())
				.build();

		return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	/**
	 * Handle validation errors from @Valid annotation
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
		log.error("Validation Exception: {}", ex.getMessage(), ex);

		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST.value())
				.message("Validation error")
				.path(getRequestPath(request))
				.timestamp(LocalDateTime.now())
				.build();

		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errorResponse.addValidationError(fieldName, errorMessage);
		});

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle constraint violations
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
		log.error("Constraint Violation Exception: {}", ex.getMessage(), ex);

		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST.value())
				.message("Validation error")
				.path(getRequestPath(request))
				.timestamp(LocalDateTime.now())
				.build();

		ex.getConstraintViolations().forEach(violation -> {
			String fieldName = violation.getPropertyPath().toString();
			String errorMessage = violation.getMessage();
			errorResponse.addValidationError(fieldName, errorMessage);
		});

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle missing request parameters
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException ex, WebRequest request) {
		log.error("Missing Parameter Exception: {}", ex.getMessage(), ex);

		String message = String.format("Parameter '%s' of type '%s' is required",
				ex.getParameterName(), ex.getParameterType());

		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST.value())
				.message(message)
				.path(getRequestPath(request))
				.timestamp(LocalDateTime.now())
				.build();

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle type mismatch exceptions
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
		log.error("Type Mismatch Exception: {}", ex.getMessage(), ex);

		String message = String.format("Parameter '%s' should be of type '%s'",
				ex.getName(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName());

		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST.value())
				.message(message)
				.path(getRequestPath(request))
				.timestamp(LocalDateTime.now())
				.build();

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle all other exceptions
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ErrorResponse> handleAllUncaughtException(Exception ex, WebRequest request) {
		log.error("Unexpected error occurred: {}", ex.getMessage(), ex);

		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message("An unexpected error occurred")
				.path(getRequestPath(request))
				.timestamp(LocalDateTime.now())
				.build();

		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Extract request path from WebRequest
	 */
	private String getRequestPath(WebRequest request) {
		String path = "";
		if (request instanceof ServletWebRequest servletWebRequest) {
			path = servletWebRequest.getRequest().getRequestURI();
		}
		return path;
	}
}