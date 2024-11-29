package rkzk.demo.tms.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import rkzk.demo.tms.model.CustomUser;

import java.beans.Transient;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @BeforeEach
    @Transactional
    void setUp() {
        try {
            CustomUser newUser = userService.getByUsername("newUser");
            userService.delete(newUser);
        } catch (UsernameNotFoundException e) { }
        try {
            CustomUser testUser = userService.getByUsername("testUser");
            userService.delete(testUser);
        } catch (UsernameNotFoundException e) { }
        try {
            CustomUser invalidUser = userService.getByUsername("invalidUser");
            userService.delete(invalidUser);
        } catch (UsernameNotFoundException e) { }
    }

    @AfterEach
    @Transactional
    void TearDown() {
        try {
            CustomUser newUser = userService.getByUsername("newUser");
            userService.delete(newUser);
        } catch (UsernameNotFoundException e) { }
        try {
            CustomUser testUser = userService.getByUsername("testUser");
            userService.delete(testUser);
        } catch (UsernameNotFoundException e) { }
        try {
            CustomUser invalidUser = userService.getByUsername("invalidUser");
            userService.delete(invalidUser);
        } catch (UsernameNotFoundException e) { }
    }

    @Test
    void signUp_shouldCreateUserAndReturnJwt() {
        // Arrange
        UserService.UserCredentials request = new UserService.UserCredentials(
                "newUser",
                "newuser@example.com",
                "securePassword123"
        );

        // Act
        String jwt = authService.signUp(request);

        // Assert
        assertNotNull(jwt, "JWT token should not be null");

        UserDetails createdUser = userService.userDetailsService().loadUserByUsername("newUser");
        assertNotNull(createdUser, "User should be created in the database");

        // Verify password is encoded
        assertTrue(passwordEncoder.matches("securePassword123", createdUser.getPassword()));
    }

    @Test
    void signIn_shouldAuthenticateAndReturnJwt() {
        // Arrange
        UserService.UserCredentials signUpRequest = new UserService.UserCredentials(
                "testUser",
                "testuser@example.com",
                "password123"
        );
        authService.signUp(signUpRequest); // Ensure user exists

        UserService.UserCredentials signInRequest = new UserService.UserCredentials(
                "testUser",
                null, // Email not required for sign-in
                "password123"
        );

        // Act
        String jwt = authService.signIn(signInRequest);

        // Assert
        assertNotNull(jwt, "JWT token should not be null");
        assertTrue(jwtService.isTokenValid(jwt, userService.userDetailsService().loadUserByUsername("testUser")));
    }

    @Test
    void signIn_shouldThrowExceptionForInvalidPassword() {
        // Arrange
        UserService.UserCredentials signUpRequest = new UserService.UserCredentials(
                "invalidUser",
                "invaliduser@example.com",
                "password123"
        );
        authService.signUp(signUpRequest); // Ensure user exists

        UserService.UserCredentials signInRequest = new UserService.UserCredentials(
                "invalidUser",
                null,
                "wrongPassword"
        );

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authService.signIn(signInRequest));
    }
}

