package com.assessment.inventory_and_order_management_system.Mapper;

import com.assessment.inventory_and_order_management_system.Model.User;
import com.assessment.inventory_and_order_management_system.Dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

	public UserDTO toDTO(User user) {
		if (user == null) {
			return null;
		}

		return UserDTO.builder()
				.id(user.getId())
				.username(user.getUsername())
				.role(user.getRole())
				.build();
	}

	public User toEntity(UserDTO userDTO) {
		if (userDTO == null) {
			return null;
		}

		User user = new User();
		user.setId(userDTO.getId());
		user.setUsername(userDTO.getUsername());
		user.setPassword(userDTO.getPassword());
		user.setRole(userDTO.getRole());

		return user;
	}
}