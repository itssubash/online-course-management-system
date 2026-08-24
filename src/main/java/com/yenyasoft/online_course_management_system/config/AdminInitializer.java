package com.yenyasoft.online_course_management_system.config;

import com.yenyasoft.online_course_management_system.enums.RoleName;
import com.yenyasoft.online_course_management_system.models.Roles;
import com.yenyasoft.online_course_management_system.models.User;
import com.yenyasoft.online_course_management_system.repositories.RolesRepository;
import com.yenyasoft.online_course_management_system.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Order(2)
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(UserRepository userRepository,
                            RolesRepository rolesRepository,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.existsByUsername("admin")) {
            return;
        }

        Roles adminRole = rolesRepository.findByRoles(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new IllegalStateException("Role ROLE_ADMIN not found"));

        User admin = User.builder()
                .username("admin")
                .email("admin@yenyasoft.com")
                .password(passwordEncoder.encode("Admin@123"))
                .firstName("System")
                .lastName("Admin")
                .enabled(true)
                .build();

        Set<Roles> roles = new HashSet<>();
        roles.add(adminRole);
        admin.setRoles(roles);

        userRepository.save(admin);
    }
}
