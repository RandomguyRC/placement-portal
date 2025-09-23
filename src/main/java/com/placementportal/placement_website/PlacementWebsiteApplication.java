package com.placementportal.placement_website;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class PlacementWebsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlacementWebsiteApplication.class, args);
	}

	// This bean runs after Spring Boot starts, and tests DB connection
	@Bean
	CommandLineRunner testDatabase(JdbcTemplate jdbcTemplate) {
		return args -> {
			try {
				Integer count = jdbcTemplate.queryForObject(
					"SELECT COUNT(*) FROM student_details", Integer.class);
				System.out.println("Number of students in DB: " + count);
			} catch (Exception e) {
				System.out.println("Database connection failed: " + e.getMessage());
			}
		};
	}
}
