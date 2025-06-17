package com.assessment.inventory_and_order_management_system.Security;

import com.assessment.inventory_and_order_management_system.Model.Order;
import com.assessment.inventory_and_order_management_system.Model.User;
import com.assessment.inventory_and_order_management_system.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserSecurity {

	private final OrderRepository orderRepository;

	/**
	 * Check if the current authenticated user is the user with the given ID
	 */
	public boolean isCurrentUser(Long userId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof User)) {
			return false;
		}

		User currentUser = (User) authentication.getPrincipal();
		return currentUser.getId().equals(userId);
	}

	/**
	 * Check if the current authenticated user has the username
	 */
	public boolean isCurrentUser(String username) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return false;
		}

		return authentication.getName().equals(username);
	}

	/**
	 * Check if the current authenticated user can access a specific order
	 */
	public boolean canAccessOrder(Long orderId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof User)) {
			return false;
		}

		User currentUser = (User) authentication.getPrincipal();
		Optional<Order> orderOpt = orderRepository.findById(orderId);

		if (orderOpt.isEmpty()) {
			return false;
		}

		Order order = orderOpt.get();
		return order.getUser().getId().equals(currentUser.getId());
	}
}