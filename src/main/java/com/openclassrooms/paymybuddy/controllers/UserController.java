package com.openclassrooms.paymybuddy.controllers;


import com.openclassrooms.paymybuddy.exception.ControllerException;
import com.openclassrooms.paymybuddy.exception.ServiceException;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.model.UserMapper;
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

/**
 * Controllers responsible for managing Users.
 */
@Controller
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Registers a new user.
     *
     * @param user  The user object populated from the registration form.
     * @param model The model to pass messages to the view.
     * @return a redirection to the login page on success, or back to the registration form on error.
     */
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

    /**
     * Retrieves a user by their email.
     *
     * @param email The email address of the user.
     * @return a ResponseEntity containing the user if found, or 404 status otherwise.
     */
    @GetMapping("/find")
    public ResponseEntity<?> findUserByEmail(@RequestParam String email) {
        Optional<User> user = userService.getUserByEmail(email);
        return user.map(value -> ResponseEntity.ok(UserMapper.toDTO(value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));    }

    /**
     * Authenticates a user using email and password.
     *
     * @param email   The user's email.
     * @param password The user's password.
     * @param request  The HTTP request, used to store security context in session.
     * @param model    The model to pass error messages to the login view.
     * @return a redirection to the home page if successful, or to the login page with error message if failed.
     */
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

    /**
     * Updates an existing user's profile information.
     *
     * @param updateUser         The user object containing updated fields.
     * @param redirectAttributes Attributes used to pass success or error messages after redirect.
     * @return a redirect to the profile page with appropriate flash message.
     */
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

    /**
     * Deletes a user by their email.
     *
     * @param email The email of the user to delete.
     * @return a ResponseEntity with a success message or throws ControllerException on failure.
     */
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
