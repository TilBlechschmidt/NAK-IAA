//package de.nordakademie.iaa.noodle.services;
//
//import de.nordakademie.iaa.noodle.services.model.UserDetails;
//import io.jsonwebtoken.Claims;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//class JWTServiceTest {
//    private JWTService jwtService;
//
//    @BeforeEach
//    public void setUp() {
//        jwtService = new JWTService();
//    }
//
//    @Test
//    void testExtractUserDetailsFromClaims() {
//        Claims claims = mock(Claims.class);
//        when(claims.get("email", String.class)).thenReturn("EMAIL");
//        when(claims.get("fullName", String.class)).thenReturn("FULL_NAME");
//
//        UserDetails userDetails = jwtService.
//    }
//}
