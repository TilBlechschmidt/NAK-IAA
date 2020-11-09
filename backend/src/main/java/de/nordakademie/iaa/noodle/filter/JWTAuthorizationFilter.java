package de.nordakademie.iaa.noodle.filter;

import de.nordakademie.iaa.noodle.services.SignInService;
import de.nordakademie.iaa.noodle.services.exceptions.AuthenticationException;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import de.nordakademie.iaa.noodle.services.exceptions.JWTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter to read the Authorization header and execute the authentication routine.
 */
@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {
    private static final String HEADER_STRING = "Authorization";

    private final SignInService signInService;

    /**
     * Creates a new JWTAuthorizationFilter.
     * @param signInService The service used to authenticate a user.
     */
    @Autowired
    public JWTAuthorizationFilter(SignInService signInService) {
        this.signInService = signInService;
    }

    /**
     * Extracts the Authorization header and updates the security context.
     * {@inheritDoc}
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            String header = getJWTHeader(request);
            Authentication authentication = signInService.springAuthenticationForHeader(header);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException | EntityNotFoundException | JWTException e) {
            SecurityContextHolder.clearContext();
        }
        chain.doFilter(request, response);
    }

    private String getJWTHeader(HttpServletRequest request) throws AuthenticationException {
        String header = request.getHeader(HEADER_STRING);
        if (header == null) { throw new AuthenticationException("missingToken"); }
        return header;
    }
}
