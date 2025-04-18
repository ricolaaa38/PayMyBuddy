package com.openclassrooms.paymybuddy.controllers;

import com.openclassrooms.paymybuddy.exception.ControllerException;
import com.openclassrooms.paymybuddy.exception.ServiceException;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.model.UserConnection;
import com.openclassrooms.paymybuddy.service.UserConnectionService;
import com.openclassrooms.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users-connections")
public class UserConnectionController {

    @Autowired
    private UserConnectionService userConnectionService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserConnection> createUserConnection(@RequestBody UserConnection userConnection) {
        try {
            UserConnection addUserConnection = userConnectionService.saveUserConnection(userConnection);
            return ResponseEntity.status(HttpStatus.CREATED).body(addUserConnection);
        } catch (ServiceException e) {
            throw new ControllerException("Failed to create users connection: " + e.getMessage(), e);
        }
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
