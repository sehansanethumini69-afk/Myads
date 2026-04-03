package com.myads;

import com.myads.model.User;
import com.myads.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class MyAdsApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(MyAdsApplication.class, args);
    }
    
    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByEmail("admin@myads.com")) {
                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("admin@myads.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(User.Role.ADMIN);
                userRepository.save(admin);
                System.out.println("Admin user created: admin@myads.com / admin123");
            }
        };
    }
}