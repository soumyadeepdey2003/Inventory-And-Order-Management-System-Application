package com.assessment.inventory_and_order_management_system.Service.Impl;

import com.assessment.inventory_and_order_management_system.Model.RefreshToken;
import com.assessment.inventory_and_order_management_system.Model.User;
import com.assessment.inventory_and_order_management_system.Repository.RefreshTokenRepository;
import com.assessment.inventory_and_order_management_system.Service.RefreshTokenService;
import com.assessment.inventory_and_order_management_system.Exception.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;

	@Value("${jwt.refresh-expiration:604800000}") // 7 days in milliseconds by default
	private long refreshTokenDurationMs;

	@Override
	public RefreshToken createRefreshToken(User user) {
		// Delete any existing refresh tokens for this user
		refreshTokenRepository.revokeAllUserTokens(user);

		RefreshToken refreshToken = RefreshToken.builder()
				.user(user)
				.token(UUID.randomUUID().toString())
				.expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
				.revoked(false)
				.build();

		return refreshTokenRepository.save(refreshToken);
	}

	@Override
	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}

	@Override
	public RefreshToken verifyExpiration(RefreshToken token) {
		if (token.isRevoked()) {
			refreshTokenRepository.delete(token);
			throw JwtException.invalidToken();
		}

		if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
			refreshTokenRepository.delete(token);
			throw JwtException.expiredToken();
		}

		return token;
	}

	@Override
	@Transactional
	public void revokeAllUserTokens(User user) {
		refreshTokenRepository.revokeAllUserTokens(user);
	}

	@Override
	public boolean isTokenValid(String token) {
		return refreshTokenRepository.existsByTokenAndRevokedFalse(token);
	}

	@Override
	@Scheduled(cron = "0 0 0 * * ?") // Run at midnight every day
	@Transactional
	public void cleanupExpiredTokens() {
		log.info("Cleaning up expired refresh tokens");
		refreshTokenRepository.deleteByExpiryDateBefore(Instant.now());
	}
}