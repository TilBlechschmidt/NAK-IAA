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

import java.util.Date;
import java.util.List;
import java.util.Optional;
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

    private static String buildEmailToken(String email, String fullName) {
        // Token does not get the TOKEN_PREFIX, because it is not used for authentication
        return baseTokenBuilder(email)
            .claim(CLAIM_EMAIL, email)
            .claim(CLAIM_FULL_NAME, fullName)
            .compact();
    }

    public Optional<AuthenticatedUser> attemptAuthentication(String email, String password) {
        return userService.getUserByEMail(email)
            .filter(user -> passwordService.isPasswordCorrect(user, password))
            .map(this::authenticateUser);
    }

    public Optional<Authentication> springAuthenticationForToken(String token) {
        return Optional.of(token)
            .filter(header -> header.startsWith(TOKEN_PREFIX))
            .map(header -> header.replaceFirst(TOKEN_PREFIX, ""))
            .flatMap(this::extractClaimsFromRawToken)
            .flatMap(this::springAuthenticationForClaims);
    }

    public Optional<User> createAccount(String token, String password) {
        // Token does not have the TOKEN_PREFIX, because it is not used for authentication
        return Optional.of(token)
            .flatMap(this::extractClaimsFromRawToken)
            .flatMap(claims -> createUserForClaims(claims, password));
    }

    public void mailSignupToken(String email, String fullName) {
        try {
            userService.getUserByEMail(email).ifPresentOrElse(
                user -> mailService.sendRegistrationMailDuplicateEmail(fullName, email),
                () -> mailService.sendRegistrationMail(buildEmailToken(email, fullName), fullName, email)
            );
        } catch (MailException e) {
            throw NoodleException.serviceUnavailable("Failed to send email");
        }
    }

    private AuthenticatedUser authenticateUser(User user) {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = baseTokenBuilder(user.getEmail())
            .claim(CLAIM_USER_ID, user.getId())
            .claim(CLAIM_AUTHORITIES,
                grantedAuthorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()))
            .compact();

        return new AuthenticatedUser(user, TOKEN_PREFIX + token);
    }

    private static JwtBuilder baseTokenBuilder(String email) {
        long currentTimestamp = System.currentTimeMillis();

        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(new Date(currentTimestamp))
            .setExpiration(new Date(currentTimestamp + EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS256, SECRET.getBytes(UTF_8));
    }

    private Optional<Claims> extractClaimsFromRawToken(String jwtToken) {
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

    private Optional<Authentication> springAuthenticationForClaims(Claims claims) {
        @SuppressWarnings("unchecked")
        List<String> authorities = claims.get(CLAIM_AUTHORITIES, List.class);
        Long userID = claims.get(CLAIM_USER_ID, Long.class);

        if (authorities == null || userID == null) {
            return Optional.empty();
        }

        return Optional.of(userID)
            .flatMap(userService::getUserByUserID)
            .map(user -> buildSpringAuthentication(user, authorities));
    }

    private Authentication buildSpringAuthentication(User user, List<String> authorities) {
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
