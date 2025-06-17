package com.assessment.inventory_and_order_management_system;

import com.assessment.inventory_and_order_management_system.Model.Role;
import com.assessment.inventory_and_order_management_system.Model.User;
import com.assessment.inventory_and_order_management_system.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class InventoryAndOrderManagementSystemApplication  implements ApplicationRunner {
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(InventoryAndOrderManagementSystemApplication.class, args);
		log.info("Inventory and Order Management System Application Started Successfully on port 8080");
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if(userRepository.findAll().stream().noneMatch(user -> user.getRole().equals(Role.ADMIN))) {
			userRepository.save(new User("admin", passwordEncoder.encode("soumyadeep2025"), Role.ADMIN));
		} else {
			log.info("Admin user already exists, skipping creation.");
		}
	}
}
