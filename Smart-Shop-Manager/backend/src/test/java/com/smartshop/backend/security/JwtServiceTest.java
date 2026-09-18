package com.smartshop.backend.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class JwtServiceTest {

    // TEST-ONLY secret — never used in any real environment.
    // Generated once for unit-test use only; rotate freely.
    private static final String TEST_ONLY_SECRET =
            "dGVzdE9ubHlTZWNyZXRGb3JVbml0VGVzdHNOb3RGb3JQcm9k";

    @Test
    void testValidSecretKeyInitialization() {
        JwtService jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "jwtSecret", TEST_ONLY_SECRET);
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", 86400000L);
        assertDoesNotThrow(jwtService::init);

        String token = jwtService.generateToken("admin");
        assertNotNull(token);
        assertEquals("admin", jwtService.extractUsername(token));
        assertTrue(jwtService.validateToken(token, "admin"));
    }

    @Test
    void testShortSecretKeyFailsFast() {
        JwtService jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "jwtSecret", "too-short-secret");
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", 86400000L);

        IllegalStateException exception = assertThrows(IllegalStateException.class, jwtService::init);
        assertTrue(exception.getMessage().contains("at least 256 bits"));
    }

    @Test
    void testNullSecretKeyFailsFast() {
        JwtService jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "jwtSecret", null);
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", 86400000L);

        assertThrows(IllegalStateException.class, jwtService::init);
    }
}
