package com.openclassrooms.paymybuddy.controllers;


import com.openclassrooms.paymybuddy.exception.ControllerException;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        try {
            User savedUser = userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            throw new ControllerException("Failed to register user: " + e.getMessage(), e);
        }
    }

    @GetMapping("/find")
    public ResponseEntity<User> findUserByEmail(@RequestParam String email) {
        Optional<User> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String email, @RequestParam String password) {
        Optional<User> user = userService.getUserByEmail(email);
//        boolean isAuthenticate = userService.authenticateUser(email, password);
//        if (isAuthenticate) {
//            return ResponseEntity.ok("Login successful");
//        } else {
//            throw new ControllerException("Invalid email or password");
//        }
    }
}
