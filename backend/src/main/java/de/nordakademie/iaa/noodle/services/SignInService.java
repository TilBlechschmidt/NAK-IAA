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

/**
 * Service to manage signing in.
 *
 * @author Noah Peeters
 * @author Hans Rißer
 */
@Service
public class SignInService {
    private static final String TOKEN_PREFIX = "Bearer ";

    private final PasswordService passwordService;
    private final UserService userService;
    private final JWTService jwtService;

    /**
     * Creates a new SignInService.
     * @param passwordService Service used to manage passwords.
     * @param userService Service used to manage users.
     * @param jwtService Service used to manage JWT tokens.
     */
    public SignInService(PasswordService passwordService, UserService userService, JWTService jwtService) {
        this.passwordService = passwordService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * Tries to authenticate a user.
     * @param email The email of the user.
     * @param password The password of the user.
     * @return An authenticated user.
     * @throws EntityNotFoundException Thrown, when the user does not exist.
     * @throws PasswordException Thrown, when the password is invalid.
     */
    public AuthenticatedUser attemptAuthentication(String email, String password)
            throws EntityNotFoundException, PasswordException {

        User user = userService.getUserByEMail(email);
        if (!passwordService.isPasswordCorrect(user, password)) {
            throw new PasswordException("invalidPassword");
        }
        return authenticateUser(user);
    }

    /**
     * Parses the Authentication data from a HTTP Header.
     * @param header THe Authorization header value of a request.
     * @return Authentication data.
     * @throws JWTException Thrown, when the JWT token is invalid.
     * @throws EntityNotFoundException Thrown, when the authenticated user does not exist.
     * @throws AuthenticationException Thrown, when the header does not contain valid data.
     */
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
