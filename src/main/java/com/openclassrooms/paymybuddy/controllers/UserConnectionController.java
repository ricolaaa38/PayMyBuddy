package com.openclassrooms.paymybuddy.controllers;

import com.openclassrooms.paymybuddy.exception.ControllerException;
import com.openclassrooms.paymybuddy.exception.ServiceException;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.model.UserConnection;
import com.openclassrooms.paymybuddy.model.UserConnectionDTO;
import com.openclassrooms.paymybuddy.model.UserMapper;
import com.openclassrooms.paymybuddy.service.UserConnectionService;
import com.openclassrooms.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller responsible for managing the relation/connection between users.
 */
@Controller
@RequestMapping("/api/users-connections")
public class UserConnectionController {

    @Autowired
    private UserConnectionService userConnectionService;

    @Autowired
    private UserService userService;

    /**
     * Creates a new user connection.
     *
     * @param user1id          the ID of the first user
     * @param user2Email       the email of the second user
     * @param redirectAttributes attributes for redirecting with messages
     * @return a redirect to the connection page with success or error message
     */
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

    /**
     * Saves a user connection.
     *
     * @param userConnection the user connection to save
     * @throws ServiceException if the connection already exists
     */
    private void saveUserConnection(UserConnection userConnection) {
        userConnectionService.saveUserConnection(userConnection);
    }

    /**
     * Retrieves a user connection based on the user IDs and email.
     *
     * @param user1id    the ID of the first user
     * @param user2Email the email of the second user
     * @return a UserConnection object
     * @throws ControllerException if either user is not found
     */
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

    /**
     * Retrieves all connections for a given user.
     *
     * @param user1Id the ID of the first user
     * @return a ResponseEntity containing a list of UserConnection objects
     * @throws ControllerException if the user is not found or if an error occurs during retrieval
     */
    @GetMapping("/")
    public ResponseEntity<List<UserConnectionDTO>> getUser1Connections(@RequestParam int user1Id) {
        try {
            User user1 = userService.getUserById(user1Id)
                    .orElseThrow(() -> new ControllerException("User not found with Id: " + user1Id));
            List<UserConnection> userConnections = userConnectionService.getUser1Connection(user1);
            List<UserConnectionDTO> connectionDTOs = userConnections.stream()
                    .map(connection -> new UserConnectionDTO(
                            UserMapper.toDTO(connection.getUser1()),
                            UserMapper.toDTO(connection.getUser2())
                    ))
                    .toList();

            return ResponseEntity.ok(connectionDTOs);
        } catch (ServiceException e) {
            throw new ControllerException("Failed to retrieve user connections: " + e.getMessage());
        }
    }

    /**
     * Deletes a user connection between two users.
     *
     * @param user1Email the email of the first user
     * @param user2Email the email of the second user
     * @return a ResponseEntity with a success message or throws ControllerException on failure
     */
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
