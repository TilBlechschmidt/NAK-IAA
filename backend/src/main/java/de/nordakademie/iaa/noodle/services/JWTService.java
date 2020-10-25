package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.filter.NoodleException;
import de.nordakademie.iaa.noodle.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static de.nordakademie.iaa.noodle.config.SecurityConstants.*;
import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class JWTService {
    private final static String CLAIM_USER_ID = "userID";
    private final static String CLAIM_AUTHORITIES = "authorities";
    private final static String CLAIM_EMAIL = "email";
    private final static String CLAIM_FULL_NAME = "fullName";

    private final UserService userService;
    private final PasswordService passwordService;
    private final MailService mailService;

    @Autowired
    public JWTService(UserService userService, PasswordService passwordService, MailService mailService) {
        this.userService = userService;
        this.passwordService = passwordService;
        this.mailService = mailService;
    }

    public Optional<AuthenticatedUser> getAuthenticatedUser(String email, String password) {
        return userService.getUserByEMail(email)
            .filter(user -> passwordService.isPasswordCorrect(user, password))
            .map(this::getAuthenticatedUserForUser);
    }

    public Optional<Authentication> getAuthentication(String token) {
        return Optional.of(token)
            .filter(header -> header.startsWith(TOKEN_PREFIX))
            .map(header -> header.replaceFirst(TOKEN_PREFIX, ""))
            .flatMap(this::getClaimsFromRawToken)
            .flatMap(this::getSpringAuthenticationForClaims);
    }

    public Optional<User> createAccount(String token, String password) {
        // Token does not have the TOKEN_PREFIX, because it is not used for authentication
        return Optional.of(token)
            .flatMap(this::getClaimsFromRawToken)
            .flatMap(claims -> createUserForClaims(claims, password));
    }

    public void sendCreateUserToken(String email, String fullName) {
        if (userService.getUserByEMail(email).isPresent()) {
            try {
                mailService.sendRegistrationMailDuplicateEmail(fullName, email);
            } catch (MailException e) {
                throw NoodleException.serviceUnavailable("Failed to send email");
            }
        } else {
            // Token does not get the TOKEN_PREFIX, because it is not used for authentication
            String token = getBaseTokenBuilder(email)
                .claim(CLAIM_EMAIL, email)
                .claim(CLAIM_FULL_NAME, fullName)
                .compact();

            try {
                mailService.sendRegistrationMail(token, fullName, email);
            } catch (MailException e) {
                throw NoodleException.serviceUnavailable("Failed to send email");
            }
        }
    }

    private AuthenticatedUser getAuthenticatedUserForUser(User user) {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = getBaseTokenBuilder(user.getEmail())
            .claim(CLAIM_USER_ID, user.getId())
            .claim(CLAIM_AUTHORITIES,
                grantedAuthorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()))
            .compact();

        return new AuthenticatedUser(user, TOKEN_PREFIX + token);
    }

    private JwtBuilder getBaseTokenBuilder(String email) {
        long currentTimestamp = System.currentTimeMillis();

        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(new Date(currentTimestamp))
            .setExpiration(new Date(currentTimestamp + EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS256, SECRET.getBytes(UTF_8));
    }

    private Optional<Claims> getClaimsFromRawToken(String jwtToken) {
        try {
            Claims claims = Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(jwtToken)
                .getBody();
            return Optional.ofNullable(claims);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Optional<Authentication> getSpringAuthenticationForClaims(Claims claims) {
        @SuppressWarnings("unchecked")
        List<String> authorities = claims.get(CLAIM_AUTHORITIES, List.class);
        Integer userID = claims.get(CLAIM_USER_ID, Integer.class);

        if (authorities == null || userID == null) {
            return Optional.empty();
        }

        return Optional.of(userID)
            .flatMap(userService::getUserByUserID)
            .map(user -> getSpringAuthentication(user, authorities));
    }

    private Authentication getSpringAuthentication(User user, List<String> authorities) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
            user.getFullName(),
            null,
            authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

        auth.setDetails(user);
        return auth;
    }

    private Optional<User> createUserForClaims(Claims claims, String password) {
        String email = claims.get(CLAIM_EMAIL, String.class);
        String fullName = claims.get(CLAIM_FULL_NAME, String.class);

        if (email == null || fullName == null) {
            return Optional.empty();
        }

        String passwordHash = passwordService.hashPassword(password);
        User user = userService.createNewUser(email, fullName, passwordHash);
        return Optional.of(user);
    }
}
