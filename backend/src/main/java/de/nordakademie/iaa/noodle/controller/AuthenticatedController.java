package de.nordakademie.iaa.noodle.controller;

import de.nordakademie.iaa.noodle.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticatedController {
    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
    }
}
