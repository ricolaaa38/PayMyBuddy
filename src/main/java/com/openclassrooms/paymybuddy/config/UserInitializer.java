package com.openclassrooms.paymybuddy.config;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class UserInitializer implements CommandLineRunner {

    private final UserService userService;

    public UserInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        String email = "lucie@gmail.com";

        if (userService.getUserByEmail(email).isEmpty()) {
            User adminUser = new User();
            adminUser.setEmail(email);
            adminUser.setUsername("lucie");
            adminUser.setPassword("azerty");
            adminUser.setSolde(100.00);

            userService.saveUser(adminUser);

            System.out.println("Default user created: " + email);
        } else {
            System.out.println("User already exists.");
        }
    }
}
