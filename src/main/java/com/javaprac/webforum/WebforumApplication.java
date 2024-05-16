package com.javaprac.webforum;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.javaprac.webforum.managers.UsersManager;
import com.javaprac.webforum.model.User;

@SpringBootApplication
public class WebforumApplication {

	static void createFirstAdmin() {
		UsersManager um = new UsersManager();
		if (um.getAll(User.class).isEmpty()) {
			User admin = new User("BOSS", "boss@webforum.net", "qwerty", List.of("admin"));
			System.out.println("Create the first administrator.");
			um.add(admin);
		}
	}

	public static void main(String[] args) {
		createFirstAdmin();
		SpringApplication.run(WebforumApplication.class, args);
	}
}
