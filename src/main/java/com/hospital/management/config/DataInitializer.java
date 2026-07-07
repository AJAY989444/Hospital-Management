package com.hospital.management.config;

import com.hospital.management.entity.User;
import com.hospital.management.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Logger;

/**
 * Runs on startup to ensure seeded admin and doctor accounts
 * always have a valid, correctly-encoded password.
 * This fixes issues where the seed.sql hash may not match
 * the production BCrypt encoder output.
 */
@Component
public class DataInitializer implements ApplicationRunner {

    private static final Logger log = Logger.getLogger(DataInitializer.class.getName());
    private static final String SEEDED_PASSWORD = "Admin123";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        List<User> seededUsers = userRepository.findByRoleIn(
                List.of(
                        com.hospital.management.entity.UserRole.ADMIN,
                        com.hospital.management.entity.UserRole.DOCTOR
                )
        );

        int fixed = 0;
        for (User user : seededUsers) {
            // Only re-encode if the current hash does NOT match the expected seeded password
            if (!passwordEncoder.matches(SEEDED_PASSWORD, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(SEEDED_PASSWORD));
                userRepository.save(user);
                log.info("Fixed password for seeded user: " + user.getEmail());
                fixed++;
            }
        }

        if (fixed > 0) {
            log.info("DataInitializer: Fixed " + fixed + " seeded account password(s).");
        } else {
            log.info("DataInitializer: All seeded account passwords are valid.");
        }
    }
}
