package com.assessment.inventory_and_order_management_system.Service;

import com.assessment.inventory_and_order_management_system.Model.User;
import com.assessment.inventory_and_order_management_system.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService  {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) throws Exception {
        try {
            log.info("Saving user: {}", user);
            User savedUser = userRepository.save(user);
            log.info("User saved: {}", savedUser);
            return savedUser;
        } catch (Exception e) {
            throw new Exception("Error saving user: " + e.getMessage());
        }
    }

    public User findUserByUsername(String username) throws Exception {
        try {
            log.info("Finding user by username: {}", username);
            Optional<User> user = userRepository.findByUsername(username);
            if (user.isEmpty()) {
                throw new Exception("User not found");
            }
            log.info("User found: {}", user);
            return user.get();
        } catch (Exception e) {
            throw new Exception("Error finding user: " + e.getMessage());
        }
    }

    public User findUserById(Long id) throws Exception {
        try {
            log.info("Finding user by id: {}", id);
            Optional<User> user = userRepository.findById(id);
            if (user.isEmpty()) {
                throw new Exception("User not found");
            }
            log.info("User found: {}", user);
            return user.get();
        } catch (Exception e) {
            throw new Exception("Error finding user: " + e.getMessage());
        }
    }

    public User updateUser(User user) throws Exception {
        try {
            log.info("Updating user: {}", user);
            User existingUser = findUserById(user.getId());
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(user.getPassword());
            existingUser.setRole(user.getRole());
            User updatedUser = userRepository.save(existingUser);
            log.info("User updated: {}", updatedUser);
            return updatedUser;
        } catch (Exception e) {
            throw new Exception("Error updating user: " + e.getMessage());
        }
    }

    public void deleteUser(Long id) throws Exception {
        try {
            log.info("Deleting user with id: {}", id);
            userRepository.deleteById(id);
            log.info("User deleted with id: {}", id);
        } catch (Exception e) {
            throw new Exception("Error deleting user: " + e.getMessage());
        }
    }
}
