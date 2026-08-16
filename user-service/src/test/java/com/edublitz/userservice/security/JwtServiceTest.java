package com.edublitz.userservice.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        /*
         * Base64 encoded secret.
         * This is only for testing.
         */
        String secret = "VGhpc0lzQVN1ZmZpY2llbnRseUxvbmdTZWNyZXRLZXlGb3JUZXN0aW5nMTIzNDU2";

        ReflectionTestUtils.setField(jwtService, "jwtSecret", secret);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 3600000L);
        ReflectionTestUtils.setField(jwtService, "refreshExpiration", 86400000L);

        userDetails = User.withUsername("test@example.com")
                .password("password")
                .roles("USER")
                .build();
    }

    @Test
    void generateToken_shouldCreateValidToken() {

        String token = jwtService.generateToken(
                userDetails,
                "user-123",
                "USER",
                "org-456"
        );

        assertNotNull(token);

        assertEquals(
                "test@example.com",
                jwtService.extractUsername(token)
        );

        assertEquals(
                "user-123",
                jwtService.extractUserId(token)
        );

        assertEquals(
                "USER",
                jwtService.extractRole(token)
        );

        assertEquals(
                "org-456",
                jwtService.extractOrgId(token)
        );
    }

    @Test
    void generateRefreshToken_shouldCreateValidToken() {

        String token = jwtService.generateRefreshToken(userDetails);

        assertNotNull(token);

        assertEquals(
                "test@example.com",
                jwtService.extractUsername(token)
        );
    }

    @Test
    void isTokenValid_shouldReturnTrueForValidToken() {

        String token = jwtService.generateToken(
                userDetails,
                "user-123",
                "USER",
                "org-456"
        );

        assertTrue(
                jwtService.isTokenValid(token, userDetails)
        );
    }

    @Test
    void isTokenValid_shouldReturnFalseForDifferentUser() {

        String token = jwtService.generateToken(
                userDetails,
                "user-123",
                "USER",
                "org-456"
        );

        UserDetails differentUser = User.withUsername("other@example.com")
                .password("password")
                .roles("USER")
                .build();

        assertFalse(
                jwtService.isTokenValid(token, differentUser)
        );
    }

    @Test
    void isTokenValid_shouldReturnFalseForInvalidToken() {

        String invalidToken = "invalid.jwt.token";

        assertFalse(
                jwtService.isTokenValid(invalidToken, userDetails)
        );
    }
}
