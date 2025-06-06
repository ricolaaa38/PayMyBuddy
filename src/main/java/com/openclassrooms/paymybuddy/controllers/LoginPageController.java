package com.openclassrooms.paymybuddy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller that handle the login page.
 */
@Controller
public class LoginPageController {

    /**
     * Displays the login page.
     *
     * @return the name of the login view.
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
