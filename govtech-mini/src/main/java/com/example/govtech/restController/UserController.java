package com.example.govtech.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.govtech.model.User;
import com.example.govtech.repository.UserRepository;

@RestController
public class UserController {
	
	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/users")
	public List<User> getUsersWithValidSalary() {
		return userRepository.findUsersWithValidSalary();
		
	}

}
