package com.assessment.inventory_and_order_management_system.Dto;

import com.assessment.inventory_and_order_management_system.Model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private Long id;

	@NotBlank(message = "Username is required")
	private String username;

	private String password;

	@NotNull(message = "Role is required")
	private Role role;
}