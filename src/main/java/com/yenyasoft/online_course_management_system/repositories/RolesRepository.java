package com.yenyasoft.online_course_management_system.repositories;

import com.yenyasoft.online_course_management_system.models.Roles;
import com.yenyasoft.online_course_management_system.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByRoles(RoleName roleName);
}
