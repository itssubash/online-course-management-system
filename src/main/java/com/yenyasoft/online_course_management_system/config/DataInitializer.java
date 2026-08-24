package com.yenyasoft.online_course_management_system.config;

import com.yenyasoft.online_course_management_system.enums.RoleName;
import com.yenyasoft.online_course_management_system.models.Roles;
import com.yenyasoft.online_course_management_system.repositories.RolesRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class DataInitializer implements CommandLineRunner {

    private final RolesRepository rolesRepository;

    public DataInitializer(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    @Override
    public void run(String... args) {
        createRoleIfNotExists(RoleName.ROLE_STUDENT);
        createRoleIfNotExists(RoleName.ROLE_INSTRUCTOR);
        createRoleIfNotExists(RoleName.ROLE_ADMIN);
    }

    private void createRoleIfNotExists(RoleName roleName) {
        if (rolesRepository.findByRoles(roleName).isEmpty()) {
            Roles role = new Roles();
            role.setRoles(roleName);
            rolesRepository.save(role);
        }
    }
}
