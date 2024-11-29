package rkzk.demo.tms.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtServiceTest {

    private JwtService jwtService;
    private SecretKey secretKey;

    @BeforeEach
    void setUp() {
        secretKey = Jwts.SIG.HS256.key().build();
        jwtService = new JwtService(secretKey, 3600000); // Токен действует 1 час
    }

    @Test
    void generateToken_shouldCreateValidJwt() {
        // Arrange
        UserDetails userDetails = new User("testUser", "password", Collections.emptyList());

        // Act
        String token = jwtService.generateToken(userDetails);

        // Assert
        assertNotNull(token, "Token should not be null");
        String extractedUsername = jwtService.extractUserName(token);
        assertEquals(userDetails.getUsername(), extractedUsername, "Extracted username should match the original username");
    }

    @Test
    void isTokenValid_shouldReturnTrueForValidToken() {
        // Arrange
        UserDetails userDetails = new User("validUser", "password", Collections.emptyList());
        String token = jwtService.generateToken(userDetails);

        // Act
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Assert
        assertTrue(isValid, "Token should be valid for the given user");
    }

    @Test
    void isTokenValid_shouldReturnFalseForExpiredToken() throws InterruptedException {
        // Arrange
        UserDetails userDetails = new User("expiredUser", "password", Collections.emptyList());
        JwtService shortLivedJwtService = new JwtService(secretKey, 1000); // Токен действует 1 секунду
        String token = shortLivedJwtService.generateToken(userDetails);

        // Ждем, чтобы токен стал недействительным
        Thread.sleep(1500);

        // Act
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Assert
        assertFalse(isValid, "Token should be invalid due to expiration");
    }

    @Test
    void extractUserName_shouldReturnCorrectUsername() {
        // Arrange
        UserDetails userDetails = new User("testUser", "password", Collections.emptyList());
        String token = jwtService.generateToken(userDetails);

        // Act
        String extractedUsername = jwtService.extractUserName(token);

        // Assert
        assertEquals(userDetails.getUsername(), extractedUsername, "Extracted username should match the original username");
    }

    @Test
    void extractClaim_shouldReturnCorrectClaims() {
        // Arrange
        UserDetails userDetails = new User("customUser", "password", Collections.emptyList());
        String token = jwtService.generateToken(userDetails);

        // Act
        Date expirationDate = jwtService.extractClaim(token, Claims::getExpiration);

        // Assert
        assertNotNull(expirationDate, "Expiration date should not be null");
        assertTrue(expirationDate.after(new Date()), "Expiration date should be in the future");
    }
}
