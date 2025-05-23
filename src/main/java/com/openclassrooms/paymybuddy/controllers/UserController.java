package com.openclassrooms.paymybuddy.controllers;


import com.openclassrooms.paymybuddy.exception.ControllerException;
import com.openclassrooms.paymybuddy.exception.ServiceException;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        if (userService.getUserByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("error", "Cet email est déjà utilisé.");
            return "register";
        }
        try {
            userService.saveUser(user);
            model.addAttribute("success", "Inscription réussie. Vous pouvez maintenant vous connecter.");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Une erreur est survenue lors de l'inscription.");
            return "register";
        }
    }

    @GetMapping("/find")
    public ResponseEntity<User> findUserByEmail(@RequestParam String email) {
        Optional<User> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            HttpServletRequest request,
                            Model model) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(email, password);
            Authentication auth = authenticationManager.authenticate(authToken);

            SecurityContextHolder.getContext().setAuthentication(auth);

            request.getSession().setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext()
            );

            return "redirect:/home";
        } catch (Exception ex) {
            model.addAttribute("error", "Email ou mot de passe invalide.");
            return "login";
        }
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute User updateUser, RedirectAttributes redirectAttributes) {
        try {
            Integer userId = updateUser.getId();
            if (userId == null) {
                throw new ControllerException("L'ID de l'utilisateur est requis pour la mise à jour.");
            }
            User previousUserInfo = userService.getUserById(userId)
                    .orElseThrow(() -> new ControllerException("Utilisateur introuvable avec id: " + userId));

            User updatedUser = userService.updateUser(updateUser, previousUserInfo);
            if (updatedUser == null) {
                redirectAttributes.addFlashAttribute("error", "Veuillez modifier un élément !");
            } else {
                redirectAttributes.addFlashAttribute("success", "Mise à jour réussie !");
            }
            return "redirect:/profil";
        } catch (ControllerException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "profil";
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
