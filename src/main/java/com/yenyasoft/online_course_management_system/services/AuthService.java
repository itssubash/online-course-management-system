package com.yenyasoft.online_course_management_system.services;

import com.yenyasoft.online_course_management_system.config.CustomUserDetails;
import com.yenyasoft.online_course_management_system.config.JwtService;
import com.yenyasoft.online_course_management_system.dtos.*;
import com.yenyasoft.online_course_management_system.enums.RoleName;
import com.yenyasoft.online_course_management_system.exceptions.AlreadyExistsException;
import com.yenyasoft.online_course_management_system.exceptions.BadRequestException;
import com.yenyasoft.online_course_management_system.models.Roles;
import com.yenyasoft.online_course_management_system.models.User;
import com.yenyasoft.online_course_management_system.repositories.RolesRepository;
import com.yenyasoft.online_course_management_system.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository,
                       RolesRepository rolesRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserResponse register(AuthRegisterRequest request) {
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
                .enabled(true)
                .build();

        user.setRoles(resolvePublicRoles(request.getRoles()));

        userRepository.save(user);

        Set<RolesResponse> roleResponses = user.getRoles().stream()
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
                .roles(roleResponses)
                .build();
    }

    public AuthResponse login(AuthLoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return buildAuthResponse(user);
    }

    public AuthResponse refresh(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        String username;
        try {
            username = jwtService.extractUsername(refreshToken);
        } catch (Exception ex) {
            throw new BadRequestException("Invalid refresh token");
        }

        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new BadRequestException("Invalid refresh token");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("User no longer exists"));

        UserDetails userDetails = new CustomUserDetails(user);

        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            throw new BadRequestException("Refresh token has expired, please log in again");
        }

        return buildAuthResponse(user);
    }

    private Set<Roles> resolvePublicRoles(Set<RoleName> requestedRoles) {
        if (requestedRoles != null && !requestedRoles.isEmpty()) {
            boolean onlyStudent = requestedRoles.size() == 1 && requestedRoles.contains(RoleName.ROLE_STUDENT);
            if (!onlyStudent) {
                throw new BadRequestException(
                        "Public registration is restricted to ROLE_STUDENT only. "
                                + "Instructor accounts are created by an administrator via /api/users.");
            }
        }

        Roles studentRole = rolesRepository.findByRoles(RoleName.ROLE_STUDENT)
                .orElseThrow(() -> new IllegalStateException("Role ROLE_STUDENT not found"));
        return Set.of(studentRole);
    }

    private AuthResponse buildAuthResponse(User user) {
        CustomUserDetails userDetails = new CustomUserDetails(user);

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        List<String> roles = user.getRoles().stream()
                .map(role -> role.getRoles().name())
                .collect(Collectors.toList());

        return AuthResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .build();
    }
}
