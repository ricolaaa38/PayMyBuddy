package com.openclassrooms.paymybuddy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller that handle the register page.
 */
@Controller
public class RegisterPageController {

    /**
     * Method to display the registration page.
     *
     * @return the name of the registration view.
     */
    @GetMapping("/register")
    public String register() {
        return "register";
    }
}
