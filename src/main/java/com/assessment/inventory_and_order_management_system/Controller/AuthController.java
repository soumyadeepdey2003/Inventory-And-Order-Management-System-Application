package com.assessment.inventory_and_order_management_system.Controller;

import com.assessment.inventory_and_order_management_system.Service.AuthService;
import com.assessment.inventory_and_order_management_system.Dto.UserDTO;
import com.assessment.inventory_and_order_management_system.Dto.Request.LoginRequest;
import com.assessment.inventory_and_order_management_system.Dto.Request.RefreshTokenRequest;
import com.assessment.inventory_and_order_management_system.Dto.Response.AuthResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
		log.info("REST request to authenticate user: {}", loginRequest.getUsername());
		AuthResponse authResponse = authService.login(loginRequest);
		return ResponseEntity.ok(authResponse);
	}

	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserDTO userDTO) {
		log.info("REST request to register user: {}", userDTO.getUsername());
		AuthResponse authResponse = authService.register(userDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
		log.info("REST request to refresh token");
		AuthResponse authResponse = authService.refreshToken(refreshTokenRequest);
		return ResponseEntity.ok(authResponse);
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			String username = authentication.getName();
			log.info("REST request to logout user: {}", username);
			authService.logout(username);
		}
		return ResponseEntity.noContent().build();
	}
}