package com.assessment.inventory_and_order_management_system.Controller;


import com.assessment.inventory_and_order_management_system.Model.User;
import com.assessment.inventory_and_order_management_system.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) throws Exception {
        try {
            log.info("Registering user: {}", user);
            return ResponseEntity.ok(userService.saveUser(user));
        }
        catch (Exception e) {
            log.error("Error registering user: {}", user, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Long id) throws Exception {
        try {
            log.info("Finding user by id: {}", id);
            return ResponseEntity.ok(userService.findUserById(id));
        }
        catch (Exception e) {
            log.error("Error finding user by id: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> findUserByUsername(@PathVariable String username) throws Exception {
        try {
            log.info("Finding user by username: {}", username);
            return ResponseEntity.ok(userService.findUserByUsername(username));
        }
        catch (Exception e) {
            log.error("Error finding user by username: {}", username, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) throws Exception {
        try {
            log.info("Updating user: {}", user);
            return ResponseEntity.ok(userService.updateUser(user));
        }
        catch (Exception e) {
            log.error("Error updating user: {}", user, e);
            return ResponseEntity.badRequest().build();
        }
    }
}
