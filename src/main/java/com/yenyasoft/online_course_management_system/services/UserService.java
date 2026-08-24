package com.yenyasoft.online_course_management_system.services;

import com.yenyasoft.online_course_management_system.dtos.*;
import com.yenyasoft.online_course_management_system.enums.RoleName;
import com.yenyasoft.online_course_management_system.exceptions.AlreadyExistsException;
import com.yenyasoft.online_course_management_system.exceptions.BadRequestException;
import com.yenyasoft.online_course_management_system.exceptions.ResourceNotFoundException;
import com.yenyasoft.online_course_management_system.models.Roles;
import com.yenyasoft.online_course_management_system.models.User;
import com.yenyasoft.online_course_management_system.repositories.RolesRepository;
import com.yenyasoft.online_course_management_system.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RolesRepository rolesRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AlreadyExistsException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Email is already in use");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .enabled(request.getEnabled() != null ? request.getEnabled() : true)
                .build();

        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            user.setRoles(resolveRoles(request.getRoles()));
        } else {
            user.setRoles(Set.of(rolesRepository.findByRoles(RoleName.ROLE_STUDENT)
                    .orElseThrow(() -> new IllegalStateException("Role ROLE_STUDENT not found"))));
        }

        userRepository.save(user);
        return toResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return toResponse(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (request.getUsername() != null) user.setUsername(request.getUsername());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPassword() != null) user.setPassword(passwordEncoder.encode(request.getPassword()));
        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if (request.getEnabled() != null) user.setEnabled(request.getEnabled());

        if (request.getRoles() != null) {
            user.setRoles(resolveRoles(request.getRoles()));
        }

        userRepository.save(user);
        return toResponse(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }

    private Set<Roles> resolveRoles(Set<RoleName> requestedRoles) {
        if (requestedRoles.contains(RoleName.ROLE_ADMIN)) {
            throw new BadRequestException(
                    "ROLE_ADMIN is pre-registered and cannot be created or assigned via the API");
        }

        Set<Roles> roles = new HashSet<>();
        for (RoleName roleName : requestedRoles) {
            roles.add(rolesRepository.findByRoles(roleName)
                    .orElseThrow(() -> new BadRequestException("Invalid role: " + roleName
                            + ". Valid roles are ROLE_STUDENT, ROLE_INSTRUCTOR")));
        }
        return roles;
    }

    private UserResponse toResponse(User user) {
        Set<RolesResponse> roles = user.getRoles().stream()
                .map(role -> new RolesResponse(role.getRoleId(), role.getRoles().name()))
                .collect(Collectors.toSet());

        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .enabled(user.getEnabled())
                .roles(roles)
                .build();
    }
}
