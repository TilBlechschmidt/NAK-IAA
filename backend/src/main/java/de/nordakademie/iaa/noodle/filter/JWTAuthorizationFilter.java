package de.nordakademie.iaa.noodle.filter;

import de.nordakademie.iaa.noodle.services.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static de.nordakademie.iaa.noodle.config.SecurityConstants.*;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {
    private final JWTService jwtService;

    @Autowired
    public JWTAuthorizationFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        getJWTToken(request)
            .flatMap(jwtService::getAuthentication)
            .ifPresentOrElse(
                SecurityContextHolder.getContext()::setAuthentication,
                SecurityContextHolder::clearContext);

        chain.doFilter(request, response);
    }

    private Optional<String> getJWTToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HEADER_STRING));
    }
}
