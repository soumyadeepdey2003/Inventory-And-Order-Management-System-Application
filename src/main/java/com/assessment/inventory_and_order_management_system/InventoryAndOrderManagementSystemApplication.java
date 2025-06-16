package com.assessment.inventory_and_order_management_system;

import com.assessment.inventory_and_order_management_system.Model.Role;
import com.assessment.inventory_and_order_management_system.Model.User;
import com.assessment.inventory_and_order_management_system.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventoryAndOrderManagementSystemApplication  implements ApplicationRunner {


	@Autowired
	private UserRepository userRepository;
	public static void main(String[] args) {
		SpringApplication.run(InventoryAndOrderManagementSystemApplication.class, args);

		System.out.println("Hello");
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		if(userRepository.findAll().stream().noneMatch(user -> user.getRole().equals(Role.ADMIN))) {
			userRepository.save(new User("admin", "soumyadeep2025", Role.ADMIN));

		}
		else {
			System.out.println("Admin already exist");
		}
	}
}
