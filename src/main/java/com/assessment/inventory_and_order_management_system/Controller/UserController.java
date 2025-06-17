package com.assessment.inventory_and_order_management_system.Controller;

import com.assessment.inventory_and_order_management_system.Service.UserService;
import com.assessment.inventory_and_order_management_system.Dto.UserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        log.info("REST request to create User: {}", userDTO);
        UserDTO result = userService.saveUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        log.info("REST request to get User by id: {}", id);
        UserDTO userDTO = userService.findUserById(id);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.info("REST request to get all Users");
        List<UserDTO> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        log.info("REST request to get User by username: {}", username);
        UserDTO userDTO = userService.findUserByUsername(username);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
        @PathVariable Long id,
        @Valid @RequestBody UserDTO userDTO) {
        log.info("REST request to update User: {}", userDTO);

        if (!id.equals(userDTO.getId())) {
            log.error("Invalid ID");
            return ResponseEntity.badRequest().build();
        }

        UserDTO result = userService.updateUser(userDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("REST request to delete User: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}