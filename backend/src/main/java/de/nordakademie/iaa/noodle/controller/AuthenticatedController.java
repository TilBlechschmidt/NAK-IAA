package de.nordakademie.iaa.noodle.controller;

import de.nordakademie.iaa.noodle.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Controller with helper functions for controllers with authenticated routes.
 */
public abstract class AuthenticatedController {
    /**
     * Gets the current authenticated user from the current request.
     * @return Authenticated User.
     */
    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
    }
}
