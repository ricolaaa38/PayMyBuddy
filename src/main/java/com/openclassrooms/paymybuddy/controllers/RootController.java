package com.openclassrooms.paymybuddy.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Root controller responsible for handling redirection based on user authentication status.
 */
@Controller
public class RootController {

    /**
     * Redirects to the appropriate page based on the user's authentication status.
     * If the user is not authenticated, redirects to the login page.
     * If the user is authenticated, redirects to the home page.
     *
     * @return a redirect URL to either the login or home page.
     */
    @GetMapping("/")
    public String redirectToAppropriatePage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }

        return "redirect:/home";
    }
}