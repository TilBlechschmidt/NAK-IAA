package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.JWTException;
import de.nordakademie.iaa.noodle.services.model.SpringAuthenticationDetails;
import de.nordakademie.iaa.noodle.services.model.UserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JWTServiceTest {
    private JWTService jwtService;

    private static String[] AUTHORITIES = {"ROLE_USER"};

    @BeforeEach
    public void setUp() {
        jwtService = new JWTService("secret", 10000);
    }

    @Test
    void testBuildEmailToken() {
        String token = jwtService.buildEmailToken("EMAIL", "FULL_NAME");

        Claims claims = Jwts.parser()
            .setSigningKey("secret".getBytes())
            .parseClaimsJws(token)
            .getBody();

        assertEquals("EMAIL", claims.get("email", String.class));
        assertEquals("FULL_NAME", claims.get("fullName", String.class));
        assertEquals("EMAIL", claims.getSubject());

        Duration duration = Duration.between(claims.getIssuedAt().toInstant(), claims.getExpiration().toInstant());
        assertEquals(10, duration.getSeconds());
    }

    @Test
    void testBuildSpringAuthenticationToken() {
        User user = mock(User.class);
        when(user.getId()).thenReturn(42L);
        when(user.getEmail()).thenReturn("EMAIL");

        String token = jwtService.buildSpringAuthenticationToken(user);

        Claims claims = Jwts.parser()
            .setSigningKey("secret".getBytes())
            .parseClaimsJws(token)
            .getBody();

        assertEquals(42L, claims.get("userID", Long.class));
        assertArrayEquals(AUTHORITIES, claims.get("authorities", List.class).toArray());
        assertEquals("EMAIL", claims.getSubject());

        Duration duration = Duration.between(claims.getIssuedAt().toInstant(), claims.getExpiration().toInstant());
        assertEquals(10, duration.getSeconds());
    }

    @Test
    void testUserDetailsForTokenInvalidToken() {
        JWTException exception = assertThrows(JWTException.class,
            () -> jwtService.userDetailsForToken("Invalid token"));

        assertEquals("invalidToken", exception.getMessage());
    }

    @Test
    void testUserDetailsForToken() throws JWTException {
        String token = jwtService.buildEmailToken("EMAIL", "FULL_NAME");
        UserDetails userDetails = jwtService.userDetailsForToken(token);

        assertEquals("EMAIL", userDetails.getEmail());
        assertEquals("FULL_NAME", userDetails.getFullName());
    }

    @Test
    void testAuthenticationDetailsForTokenInvalidToken() {
        JWTException exception = assertThrows(JWTException.class,
            () -> jwtService.authenticationDetailsForToken("Invalid token"));

        assertEquals("invalidToken", exception.getMessage());
    }

    @Test
    void testAuthenticationDetailsForToken() throws JWTException {
        User user = mock(User.class);
        when(user.getId()).thenReturn(42L);
        when(user.getEmail()).thenReturn("EMAIL");

        String token = jwtService.buildSpringAuthenticationToken(user);
        SpringAuthenticationDetails springAuthenticationDetails = jwtService.authenticationDetailsForToken(token);

        assertEquals(42L, springAuthenticationDetails.getUserID());
        assertArrayEquals(AUTHORITIES, springAuthenticationDetails.getAuthorities().toArray());
    }
}
