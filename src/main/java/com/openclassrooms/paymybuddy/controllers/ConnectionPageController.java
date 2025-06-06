package com.openclassrooms.paymybuddy.controllers;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.model.UserMapper;
import com.openclassrooms.paymybuddy.security.CustomUserDetails;
import com.openclassrooms.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *Controller responsible for handling the user connection (relation) page.
 */
@Controller
public class ConnectionPageController {

    @Autowired
    private UserService userService;

    /**
     * Displays the connection page for the authenticated user.
     *
     * @param currentUserDetails the details of the currently authenticated user
     * @param model              the model to pass attributes to the view
     * @return the name of the view to render
     */
    @GetMapping("/connection")
    public String connectionPage(@AuthenticationPrincipal CustomUserDetails currentUserDetails, Model model) {
        if (currentUserDetails == null) {
            throw new RuntimeException("Utilisateur non authentifié");
        }
        User user = userService.getUserById(currentUserDetails.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        model.addAttribute("user", UserMapper.toDTO(user));
        model.addAttribute("currentPath", "/connection");
        return "connection";
    }
}