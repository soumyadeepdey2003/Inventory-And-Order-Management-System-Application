package com.assessment.inventory_and_order_management_system.Service;

import com.assessment.inventory_and_order_management_system.Dto.UserDTO;

import java.util.List;

public interface UserService {
	UserDTO saveUser(UserDTO userDTO);
	UserDTO findUserByUsername(String username);
	UserDTO findUserById(Long id);
	UserDTO updateUser(UserDTO userDTO);
	void deleteUser(Long id);
	List<UserDTO> findAllUsers();
}