package com.openclassrooms.paymybuddy.controllers;


import com.openclassrooms.paymybuddy.exception.ControllerException;
import com.openclassrooms.paymybuddy.exception.ServiceException;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        if (userService.getUserByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("error", "Cet email est déjà utilisé.");
            return "register"; // Retourne la page d'inscription avec un message d'erreur
        }
        try {
            userService.saveUser(user);
            model.addAttribute("success", "Inscription réussie. Vous pouvez maintenant vous connecter.");
            return "redirect:/login"; // Redirige vers la page de connexion
        } catch (Exception e) {
            model.addAttribute("error", "Une erreur est survenue lors de l'inscription.");
            return "register"; // Retourne la page d'inscription avec un message d'erreur
        }
    }

    @GetMapping("/find")
    public ResponseEntity<User> findUserByEmail(@RequestParam String email) {
        Optional<User> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, Model model) {
        try {
            User user = userService.getUserByEmail(email)
                    .orElseThrow(() -> new ControllerException("Utilisateur introuvable avec cet email : " + email));

            if (userService.comparePasswords(password, user.getPassword())) {
                model.addAttribute("success", "Connexion réussie !");
                return "redirect:/home";
            } else {
                model.addAttribute("error", "Mot de passe invalide.");
                return "login";
            }
        } catch (ControllerException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User updateUser, @RequestParam String email) {
        try {
            User previousUserInfo = userService.getUserByEmail(email)
                    .orElseThrow(() -> new ControllerException("User not found with email: " + email));
            User updatedUser = userService.updateUser(updateUser, previousUserInfo);
            return ResponseEntity.ok(updatedUser);
        } catch (ServiceException e) {
            throw new ControllerException("Error while updating user: " + e.getMessage(), e);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam String email) {
        try {
            User user = userService.getUserByEmail(email)
                    .orElseThrow(() -> new ControllerException("User not found with email: " + email));
            userService.deleteUser(user);
            return ResponseEntity.ok("User deleted successfully");
        } catch (ServiceException e) {
            throw new ControllerException("Unexpected error: " + e.getMessage());
        }
    }
}
