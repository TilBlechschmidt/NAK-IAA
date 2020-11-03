package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.JWTException;
import de.nordakademie.iaa.noodle.services.model.SpringAuthenticationDetails;
import de.nordakademie.iaa.noodle.services.model.UserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static de.nordakademie.iaa.noodle.config.SecurityConstants.*;
import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class JWTService {
    private final static String CLAIM_USER_ID = "userID";
    private final static String CLAIM_AUTHORITIES = "authorities";
    private final static String CLAIM_EMAIL = "email";
    private final static String CLAIM_FULL_NAME = "fullName";

    private static JwtBuilder baseTokenBuilder(String email) {
        long currentTimestamp = System.currentTimeMillis();

        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(new Date(currentTimestamp))
            .setExpiration(new Date(currentTimestamp + EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS256, SECRET.getBytes(UTF_8));
    }

    public String buildEmailToken(String email, String fullName) {
        // Token does not get the TOKEN_PREFIX, because it is not used for authentication
        return baseTokenBuilder(email)
            .claim(CLAIM_EMAIL, email)
            .claim(CLAIM_FULL_NAME, fullName)
            .compact();
    }

    public String buildSpringAuthenticationToken(User user) {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

        return TOKEN_PREFIX + baseTokenBuilder(user.getEmail())
            .claim(CLAIM_USER_ID, user.getId())
            .claim(CLAIM_AUTHORITIES,
                grantedAuthorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()))
            .compact();
    }

    public UserDetails userDetailsForToken(String token) throws JWTException {
        Claims claims = extractClaimsFromToken(token);
        return extractUserDetailsFromClaims(claims);
    }

    public SpringAuthenticationDetails authenticationDetailsForToken(String token) throws JWTException {
        Claims claims = extractClaimsFromToken(token);
        return extractAuthenticationDetailsFromClaims(claims);
    }


    private Claims extractClaimsFromToken(String jwtToken) throws JWTException {
        try {
            return Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(jwtToken)
                .getBody();
        } catch (Exception e) {
            throw new JWTException("invalidToken");
        }
    }

    private SpringAuthenticationDetails extractAuthenticationDetailsFromClaims(Claims claims) throws JWTException {
        @SuppressWarnings("unchecked")
        List<String> authorities = claims.get(CLAIM_AUTHORITIES, List.class);
        Long userID = claims.get(CLAIM_USER_ID, Long.class);

        if (authorities == null || userID == null) {
            throw new JWTException("missingClaims");
        }

        return new SpringAuthenticationDetails(userID, authorities);
    }


    private UserDetails extractUserDetailsFromClaims(Claims claims) throws JWTException {
        String email = claims.get(CLAIM_EMAIL, String.class);
        String fullName = claims.get(CLAIM_FULL_NAME, String.class);

        if (email == null || fullName == null) {
            throw new JWTException("missingClaims");
        }
        return new UserDetails(email, fullName);
    }
}
