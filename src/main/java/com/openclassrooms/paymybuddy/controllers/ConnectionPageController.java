package com.openclassrooms.paymybuddy.controllers;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.security.CustomUserDetails;
import com.openclassrooms.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConnectionPageController {

    @Autowired
    private UserService userService;

    @GetMapping("/connection")
    public String connectionPage(@AuthenticationPrincipal CustomUserDetails currentUserDetails, Model model) {
        if (currentUserDetails == null) {
            throw new RuntimeException("Utilisateur non authentifié");
        }
        // Récupérer l'utilisateur connecté
        User user = userService.getUserById(currentUserDetails.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        model.addAttribute("user", user);
        model.addAttribute("currentPath", "/connection");
        return "connection";
    }
}