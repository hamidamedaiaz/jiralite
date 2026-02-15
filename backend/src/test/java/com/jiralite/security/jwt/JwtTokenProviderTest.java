package com.jiralite.security.jwt;

import com.jiralite.enums.Role;
import com.jiralite.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {
    private JwtTokenProvider jwtTokenProvider;
    private User userTest;

    @BeforeEach
    void setUp() throws Exception {
        jwtTokenProvider = new JwtTokenProvider();

        // set private fields injected with @Value using reflection for unit testing
        Field secretField = JwtTokenProvider.class.getDeclaredField("secretKey");
        secretField.setAccessible(true);
        // key must be long enough for HS256 (at least 32 bytes)
        secretField.set(jwtTokenProvider, "my-very-strong-secret-key-which-is-32-bytes!!");

        Field expField = JwtTokenProvider.class.getDeclaredField("expiration");
        expField.setAccessible(true);
        expField.set(jwtTokenProvider, 3600000L); // 1 hour

        userTest = new User();
        userTest.setId(1L);
        userTest.setUsername("testuser");
        userTest.setEmail("hamidamediaz@gmail.com");
        userTest.setRole(Role.DEVELOPER);
    }


    @Test
    void testGenerateToken() {
        String token = jwtTokenProvider.generateToken(userTest);
        System.out.println("Generated Token: " + token);

        assertThat(token).isNotNull();
        // validate token and claims
        boolean valid = jwtTokenProvider.validateToken(token);
        assertThat(valid).isTrue();

        String username = jwtTokenProvider.getUsernameFromToken(token);
        assertThat(username).isEqualTo(userTest.getUsername());
    }
}