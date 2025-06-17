package com.assessment.inventory_and_order_management_system.Service.Impl;

import com.assessment.inventory_and_order_management_system.Model.RefreshToken;
import com.assessment.inventory_and_order_management_system.Model.User;
import com.assessment.inventory_and_order_management_system.Repository.UserRepository;
import com.assessment.inventory_and_order_management_system.Security.JwtService;
import com.assessment.inventory_and_order_management_system.Service.AuthService;
import com.assessment.inventory_and_order_management_system.Service.RefreshTokenService;
import com.assessment.inventory_and_order_management_system.Dto.UserDTO;
import com.assessment.inventory_and_order_management_system.Dto.Request.LoginRequest;
import com.assessment.inventory_and_order_management_system.Dto.Request.RefreshTokenRequest;
import com.assessment.inventory_and_order_management_system.Dto.Response.AuthResponse;
import com.assessment.inventory_and_order_management_system.Exception.AuthenticationException;
import com.assessment.inventory_and_order_management_system.Exception.DuplicateResourceException;
import com.assessment.inventory_and_order_management_system.Exception.ResourceNotFoundException;
import com.assessment.inventory_and_order_management_system.Mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final RefreshTokenService refreshTokenService;
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final BCryptPasswordEncoder passwordEncoder;

	@Value("${jwt.expiration}")
	private long jwtExpirationMs;

	@Override
	@Transactional
	public AuthResponse login(LoginRequest loginRequest) {
		log.info("Authenticating user: {}", loginRequest.getUsername());

		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							loginRequest.getUsername(),
							loginRequest.getPassword()
					)
			);

			SecurityContextHolder.getContext().setAuthentication(authentication);
			User user = (User) authentication.getPrincipal();

			String accessToken = jwtService.generateToken(user);
			RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

			return AuthResponse.builder()
					.accessToken(accessToken)
					.refreshToken(refreshToken.getToken())
					.username(user.getUsername())
					.role(user.getRole())
					.expiresIn(jwtExpirationMs / 1000) // Convert to seconds
					.build();
		} catch (BadCredentialsException e) {
			throw AuthenticationException.invalidCredentials();
		}
	}

	@Override
	@Transactional
	public AuthResponse register(UserDTO userDTO) {
		log.info("Registering new user: {}", userDTO.getUsername());

		// Check if username already exists
		if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
			throw DuplicateResourceException.forField("User", "username", userDTO.getUsername());
		}

		// Encode password
		userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

		// Save user
		User user = userMapper.toEntity(userDTO);
		User savedUser = userRepository.save(user);

		// Generate tokens
		String accessToken = jwtService.generateToken(savedUser);
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedUser);

		return AuthResponse.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken.getToken())
				.username(savedUser.getUsername())
				.role(savedUser.getRole())
				.expiresIn(jwtExpirationMs / 1000) // Convert to seconds
				.build();
	}

	@Override
	@Transactional
	public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
		log.info("Refreshing token");

		return refreshTokenService.findByToken(refreshTokenRequest.getRefreshToken())
				.map(refreshTokenService::verifyExpiration)
				.map(RefreshToken::getUser)
				.map(user -> {
					String accessToken = jwtService.generateToken(user);

					return AuthResponse.builder()
							.accessToken(accessToken)
							.refreshToken(refreshTokenRequest.getRefreshToken())
							.username(user.getUsername())
							.role(user.getRole())
							.expiresIn(jwtExpirationMs / 1000) // Convert to seconds
							.build();
				})
				.orElseThrow(AuthenticationException::invalidToken);
	}

	@Override
	@Transactional
	public void logout(String username) {
		log.info("Logging out user: {}", username);

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> ResourceNotFoundException.forField("User", "username", username));

		refreshTokenService.revokeAllUserTokens(user);
		SecurityContextHolder.clearContext();
	}
}