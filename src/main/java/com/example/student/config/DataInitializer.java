package com.example.student.config;

import com.example.student.entity.AppUser;
import com.example.student.entity.Role;
import com.example.student.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        createIfMissing("admin@test.com", "admin123", Role.ADMIN);
        createIfMissing("user@test.com", "user123", Role.USER);
    }

    private void createIfMissing(String email, String rawPassword, Role role) {
        if (!appUserRepository.existsByEmail(email)) {
            appUserRepository.save(new AppUser(email, passwordEncoder.encode(rawPassword), role));
        }
    }
}