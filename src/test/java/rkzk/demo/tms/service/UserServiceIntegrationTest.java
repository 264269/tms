package rkzk.demo.tms.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import rkzk.demo.tms.model.CustomUser;
import rkzk.demo.tms.model.persistent.Role;
import rkzk.demo.tms.repository.CustomUserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomUserRepository customUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        customUserRepository.deleteAll();
    }

    @Test
    void create_shouldCreateUserWhenDataIsValid() {
        // Given
        UserService.UserCredentials userCredentials = new UserService.UserCredentials(
                "testUser", "test@example.com", passwordEncoder.encode("password")
        );

        // When
        CustomUser createdUser = userService.create(userCredentials);

        // Then
        assertNotNull(createdUser.getUserId());
        assertEquals("testUser", createdUser.getUsername());
        assertEquals("test@example.com", createdUser.getEmail());
        assertTrue(passwordEncoder.matches("password", createdUser.getPassword()));
        assertEquals(1, customUserRepository.count());
    }

    @Test
    void create_shouldThrowExceptionWhenEmailAlreadyExists() {
        // Given
        customUserRepository.save(
                CustomUser.builder().username("user1").email("duplicate@example.com").password("password").build()
        );

        UserService.UserCredentials userCredentials = new UserService.UserCredentials(
                "testUser", "duplicate@example.com", "password"
        );

        // When & Then
        assertThrows(RuntimeException.class, () -> userService.create(userCredentials));
    }

    @Test
    void getById_shouldReturnUserWhenIdExists() {
        // Given
        CustomUser user = customUserRepository.save(
                CustomUser.builder().username("user1").email("user1@example.com").password("password").build()
        );

        // When
        CustomUser fetchedUser = userService.getById(user.getUserId());

        // Then
        assertEquals(user.getUserId(), fetchedUser.getUserId());
        assertEquals("user1", fetchedUser.getUsername());
    }

    @Test
    void getById_shouldThrowExceptionWhenIdNotExists() {
        // Given
        Long nonExistentId = 999L;

        // When & Then
        assertThrows(RuntimeException.class, () -> userService.getById(nonExistentId));
    }

    @Test
    @WithMockUser(username = "owner", roles = {"user"})
    void checkAccessToUser_shouldReturnTrueForSameUserId() {
        // Given
        List<Role> roles = new ArrayList<>();
        roles.add(Role.RoleEnum.USER.getRole());
        CustomUser user = customUserRepository.save(
                CustomUser.builder().username("owner").email("owner@example.com").password("password").roles(roles).build()
        );

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
        );

        // When
        boolean hasAccess = userService.checkAccessToUser(user.getUserId());

        // Then
        assertTrue(hasAccess);
    }

    @Test
    void deleteByUsername_shouldRemoveUser() {
        // Given
        CustomUser user = customUserRepository.save(
                CustomUser.builder().username("userToDelete").email("delete@example.com").password("password").build()
        );

        // When
        userService.deleteByUsername("userToDelete");

        // Then
        assertFalse(customUserRepository.existsByUsername("userToDelete"));
    }

}
