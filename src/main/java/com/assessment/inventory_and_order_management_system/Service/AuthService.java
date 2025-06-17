package com.assessment.inventory_and_order_management_system.Service;

import com.assessment.inventory_and_order_management_system.Dto.UserDTO;
import com.assessment.inventory_and_order_management_system.Dto.Request.LoginRequest;
import com.assessment.inventory_and_order_management_system.Dto.Request.RefreshTokenRequest;
import com.assessment.inventory_and_order_management_system.Dto.Response.AuthResponse;

public interface AuthService {
	AuthResponse login(LoginRequest loginRequest);
	AuthResponse register(UserDTO userDTO);
	AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
	void logout(String username);
}