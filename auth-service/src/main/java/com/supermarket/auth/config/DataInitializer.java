package com.supermarket.auth.config;

import com.supermarket.auth.entity.AppUser;
import com.supermarket.auth.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(AppUserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            repository.save(new AppUser(null, "user", passwordEncoder.encode("password"), "ROLE_USER"));
            repository.save(new AppUser(null, "admin", passwordEncoder.encode("admin"), "ROLE_USER,ROLE_ADMIN"));
        }
    }
}
