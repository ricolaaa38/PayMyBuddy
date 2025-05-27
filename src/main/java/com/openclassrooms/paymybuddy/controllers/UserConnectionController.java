package com.openclassrooms.paymybuddy.controllers;

import com.openclassrooms.paymybuddy.exception.ControllerException;
import com.openclassrooms.paymybuddy.exception.ServiceException;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.model.UserConnection;
import com.openclassrooms.paymybuddy.service.UserConnectionService;
import com.openclassrooms.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/api/users-connections")
public class UserConnectionController {

    @Autowired
    private UserConnectionService userConnectionService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public String createUserConnection(@RequestParam int user1id, @RequestParam String user2Email, RedirectAttributes redirectAttributes) {
        try {
            UserConnection userConnection = getUserConnection(user1id, user2Email);
            saveUserConnection(userConnection);
            redirectAttributes.addFlashAttribute("success", "Connexion ajoutée avec succès !");
            return "redirect:/connection";
        } catch (ServiceException | ControllerException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/connection";
        }
    }

    private void saveUserConnection(UserConnection userConnection) {
        userConnectionService.saveUserConnection(userConnection);
    }

    private UserConnection getUserConnection(int user1id, String user2Email) {
        User user1 = userService.getUserById(user1id)
                .orElseThrow(() -> new ControllerException("Utilisateur connecté introuvable avec l'ID: " + user1id));
        User user2 = userService.getUserByEmail(user2Email)
                .orElseThrow(() -> new ControllerException("Utilisateur introuvable avec l'email: " + user2Email));

        UserConnection userConnection = new UserConnection();
        userConnection.setUser1(user1);
        userConnection.setUser2(user2);
        return userConnection;
    }

    @GetMapping("/")
    public ResponseEntity<List<UserConnection>> getUser1Connections(@RequestParam int user1Id) {
        try {
            User user1 = userService.getUserById(user1Id)
                    .orElseThrow(() -> new ControllerException("User not found with Id: " + user1Id));
            List<UserConnection> userConnections = userConnectionService.getUser1Connection(user1);
            return ResponseEntity.ok(userConnections);
        } catch (ServiceException e) {
            throw new ControllerException("Failed to retrieve user connections: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUserConnection(@RequestParam String user1Email, @RequestParam String user2Email) {
        try {
            User user1 = userConnectionService.verifyUserByEmail(user1Email);
            User user2 = userConnectionService.verifyUserByEmail(user2Email);

            UserConnection connection = userConnectionService.getUserConnectionByUsers(user1, user2)
                    .orElseThrow(() -> new ControllerException("Connection not found between the specified users"));

            userConnectionService.deleteUserConnection(connection);
            return ResponseEntity.ok("Connection successfully deleted");
        } catch (ServiceException e) {
            throw new ControllerException("Failed to delete user connection: " + e.getMessage());
        }
    }
}
