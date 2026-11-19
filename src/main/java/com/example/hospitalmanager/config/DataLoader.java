
package com.example.hospitalmanager.config;

import com.example.hospitalmanager.model.AppUser;
import com.example.hospitalmanager.model.Role;
import com.example.hospitalmanager.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initializeUsers(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            // Create ADMIN account
            if (userRepository.findByUsername("admin").isEmpty()) {

                AppUser admin = new AppUser();
                admin.setUsername("admin");
                admin.setPasswordHash(
                        passwordEncoder.encode("Admin@123"));
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);
            }

            // Create DOCTOR account
            if (userRepository.findByUsername("doctor1").isEmpty()) {

                AppUser doctor = new AppUser();
                doctor.setUsername("doctor1");
                doctor.setPasswordHash(
                        passwordEncoder.encode("Doctor@123"));
                doctor.setRole(Role.DOCTOR);

                userRepository.save(doctor);
            }
        };
    }
}
