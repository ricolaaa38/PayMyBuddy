package com.openclassrooms.paymybuddy.controllers;

import com.openclassrooms.paymybuddy.exception.ControllerException;
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
        } catch (Exception e) {
            throw new ControllerException("Failed to create users connection: " + e.getMessage(), e);
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<UserConnection>> getUser1Connections(@RequestParam int user1Id) {
        User user1 = userService.getUserById(user1Id).orElseThrow(() -> new RuntimeException("User not found"));
        List<UserConnection> userConnections = userConnectionService.getUser1Connection(user1);
        return ResponseEntity.ok(userConnections);
    }
}
