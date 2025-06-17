package com.assessment.inventory_and_order_management_system.Service.Impl;

import com.assessment.inventory_and_order_management_system.Model.Order;
import com.assessment.inventory_and_order_management_system.Model.User;
import com.assessment.inventory_and_order_management_system.Repository.OrderRepository;
import com.assessment.inventory_and_order_management_system.Repository.RefreshTokenRepository;
import com.assessment.inventory_and_order_management_system.Repository.UserRepository;
import com.assessment.inventory_and_order_management_system.Service.UserService;
import com.assessment.inventory_and_order_management_system.Dto.UserDTO;
import com.assessment.inventory_and_order_management_system.Exception.DuplicateResourceException;
import com.assessment.inventory_and_order_management_system.Exception.ResourceNotFoundException;
import com.assessment.inventory_and_order_management_system.Mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String USERNAME_FIELD = "username";
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OrderRepository orderRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO saveUser(UserDTO userDTO) {
        log.info("Saving user: {}", userDTO);

        // Check if username already exists
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw DuplicateResourceException.forField("User", USERNAME_FIELD, userDTO.getUsername());
        }

        // Encode password before saving
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        User user = userMapper.toEntity(userDTO);
        User savedUser = userRepository.save(user);

        log.info("User saved with ID: {}", savedUser.getId());
        return userMapper.toDTO(savedUser);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#username)")
    public UserDTO findUserByUsername(String username) {
        log.info("Finding user by username: {}", username);

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> ResourceNotFoundException.forField("User", USERNAME_FIELD, username));

        return userMapper.toDTO(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id)")
    public UserDTO findUserById(Long id) {
        log.info("Finding user by id: {}", id);

        User user = userRepository.findById(id)
            .orElseThrow(() -> ResourceNotFoundException.forId("User", id));

        return userMapper.toDTO(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#userDTO.id)")
    public UserDTO updateUser(UserDTO userDTO) {
        log.info("Updating user: {}", userDTO);

        // Check if user exists
        userRepository.findById(userDTO.getId())
            .orElseThrow(() -> ResourceNotFoundException.forId("User", userDTO.getId()));

        // Check if username is being changed and already exists
        User existingUser = userRepository.findById(userDTO.getId()).get();
        if (!existingUser.getUsername().equals(userDTO.getUsername()) &&
            userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw DuplicateResourceException.forField("User", USERNAME_FIELD, userDTO.getUsername());
        }

        // If password is provided, encode it
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        } else {
            // Keep existing password if not provided
            userDTO.setPassword(existingUser.getPassword());
        }

        User user = userMapper.toEntity(userDTO);
        User updatedUser = userRepository.save(user);

        log.info("User updated: {}", updatedUser);
        return userMapper.toDTO(updatedUser);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);

        // Check if user exists
        User user = userRepository.findById(id)
            .orElseThrow(() -> ResourceNotFoundException.forId("User", id));

        // Delete associated refresh tokens
        refreshTokenRepository.deleteByUser(user);

        // Delete associated orders
        List< Order > userOrders = orderRepository.findByUserId(user.getId());
        if ( !userOrders.isEmpty() ) {
            log.warn("User with id {} has associated orders. Deleting orders before user deletion.", id);
            orderRepository.deleteAll(userOrders);
        }

        // Delete user
        userRepository.deleteById(id);
        log.info("User deleted with id: {}", id);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> findAllUsers() {
        log.info("Finding all users");

        List<User> users = userRepository.findAll();
        return users.stream()
            .map(userMapper::toDTO)
            .toList();
    }
}