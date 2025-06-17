package com.assessment.inventory_and_order_management_system.Service.Impl;

import com.assessment.inventory_and_order_management_system.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username: {}", username);

        return userRepository.findByUsername(username)
            .orElseThrow(() -> {
                log.error("User not found with username: {}", username);
                return new UsernameNotFoundException("User not found with username: " + username);
            });
    }
}