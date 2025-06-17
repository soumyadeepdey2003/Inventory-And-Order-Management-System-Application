package com.assessment.inventory_and_order_management_system.Service;

import com.assessment.inventory_and_order_management_system.Model.RefreshToken;
import com.assessment.inventory_and_order_management_system.Model.User;

import java.util.Optional;

public interface RefreshTokenService {

	RefreshToken createRefreshToken(User user);

	Optional<RefreshToken> findByToken(String token);

	RefreshToken verifyExpiration(RefreshToken token);

	void revokeAllUserTokens(User user);

	boolean isTokenValid(String token);

	void cleanupExpiredTokens();
}