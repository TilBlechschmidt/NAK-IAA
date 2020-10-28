package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static de.nordakademie.iaa.noodle.config.SecurityConstants.TOKEN_PREFIX;

@Component
public class SignInService {

    private final PasswordService passwordService;
    private final UserService userService;
    private final JWTService jwtService;

    public SignInService(PasswordService passwordService, UserService userService, JWTService jwtService) {
        this.passwordService = passwordService;
        this.userService = userService;
        this.jwtService = jwtService;
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
            .flatMap(jwtService::extractClaimsFromToken)
            .flatMap(jwtService::extractSpringAuthenticationDetailsFromClaims)
            .flatMap(this::sprintAuthenticationFromDetails);
    }

    private Optional<Authentication> sprintAuthenticationFromDetails(SpringAuthenticationDetails details) {
       return Optional.of(details.userID)
                .flatMap(userService::getUserByUserID)
                .map(user -> buildSpringAuthentication(user, details.authorities));
    }

    private AuthenticatedUser authenticateUser(User user) {
        return new AuthenticatedUser(user, jwtService.buildSpringAuthenticationToken(user));
    }

    private Authentication buildSpringAuthentication(User user, List<String> authorities) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
            user.getFullName(),
            null,
            authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        auth.setDetails(user);
        return auth;
    }
}
