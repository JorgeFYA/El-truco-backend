package com.marketminds.ecommerce.elTruco.config;

import com.marketminds.ecommerce.elTruco.models.Role;
import com.marketminds.ecommerce.elTruco.models.Users;
import com.marketminds.ecommerce.elTruco.repositories.UsersRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (usersRepository.findByEmail("admin@marketminds.com").isEmpty()) {
            Users admin = new Users();
            admin.setName("Admin");
            admin.setLastName("Root");
            admin.setEmail("admin@marketminds.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);

            usersRepository.save(admin);
        }
    }
}