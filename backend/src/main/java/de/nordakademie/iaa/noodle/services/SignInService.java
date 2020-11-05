package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.AuthenticationException;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import de.nordakademie.iaa.noodle.services.exceptions.JWTException;
import de.nordakademie.iaa.noodle.services.exceptions.PasswordException;
import de.nordakademie.iaa.noodle.services.model.AuthenticatedUser;
import de.nordakademie.iaa.noodle.services.model.SpringAuthenticationDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SignInService {
    private static final String TOKEN_PREFIX = "Bearer ";

    private final PasswordService passwordService;
    private final UserService userService;
    private final JWTService jwtService;

    public SignInService(PasswordService passwordService, UserService userService, JWTService jwtService) {
        this.passwordService = passwordService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    public AuthenticatedUser attemptAuthentication(String email, String password)
            throws EntityNotFoundException, PasswordException {

        User user = userService.getUserByEMail(email);
        if (!passwordService.isPasswordCorrect(user, password)) {
            throw new PasswordException("invalidPassword");
        }
        return authenticateUser(user);
    }

    public Authentication springAuthenticationForHeader(String header)
        throws JWTException, EntityNotFoundException, AuthenticationException {

        if (!header.startsWith(TOKEN_PREFIX)) {
            throw new AuthenticationException("invalidHeaderFormat");
        }
        String token = header.replaceFirst(TOKEN_PREFIX, "");
        SpringAuthenticationDetails authenticationDetails = jwtService.authenticationDetailsForToken(token);
        return springAuthenticationFromDetails(authenticationDetails);
    }

    private Authentication springAuthenticationFromDetails(SpringAuthenticationDetails details) throws EntityNotFoundException {
        User user = userService.getUserByUserID(details.getUserID());
        return buildSpringAuthentication(user, details.getAuthorities());
    }

    private AuthenticatedUser authenticateUser(User user) {
        String headerToken = TOKEN_PREFIX + jwtService.buildSpringAuthenticationToken(user);
        return new AuthenticatedUser(user, headerToken);
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
