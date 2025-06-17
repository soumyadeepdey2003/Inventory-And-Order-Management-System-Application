package com.assessment.inventory_and_order_management_system.Dto.Response;

import com.assessment.inventory_and_order_management_system.Model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
	private String accessToken;
	private String refreshToken;
	private String username;
	private Role role;
	private long expiresIn;
}