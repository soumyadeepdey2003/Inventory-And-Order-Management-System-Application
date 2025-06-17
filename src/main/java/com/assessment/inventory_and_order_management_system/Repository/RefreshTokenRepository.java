package com.assessment.inventory_and_order_management_system.Repository;

import com.assessment.inventory_and_order_management_system.Model.RefreshToken;
import com.assessment.inventory_and_order_management_system.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	Optional<RefreshToken> findByToken(String token);

	List<RefreshToken> findByUser(User user);

	@Modifying
	@Query("UPDATE RefreshToken r SET r.revoked = true WHERE r.user = :user")
	void revokeAllUserTokens(User user);

	boolean existsByTokenAndRevokedFalse(String token);

	@Modifying
	void deleteByExpiryDateBefore(java.time.Instant now);

	void deleteByUser ( User user );
}