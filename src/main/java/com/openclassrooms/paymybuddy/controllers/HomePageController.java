package com.openclassrooms.paymybuddy.controllers;

import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.model.UserConnection;
import com.openclassrooms.paymybuddy.security.CustomUserDetails;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UserConnectionService;
import com.openclassrooms.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomePageController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserConnectionService userConnectionService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/home")

        public String homePage(@AuthenticationPrincipal CustomUserDetails currentUserDetails, Model model) {
            if (currentUserDetails == null) {
                throw new RuntimeException("Utilisateur non authentifié");
            }

            User user = userService.getUserById(currentUserDetails.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
            List<UserConnection> connections = userConnectionService.getUser1Connection(user);
            List<Transaction> sentTransactions = transactionService.getTransactionBySender(user);
            List<Transaction> receivedTransactions = transactionService.getTransactionByReceiver(user);

            List<Transaction> allTransactions = new java.util.ArrayList<>();
            allTransactions.addAll(sentTransactions);
            allTransactions.addAll(receivedTransactions);
            allTransactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));

            model.addAttribute("user", user);
            model.addAttribute("connections", connections);
            model.addAttribute("transactions", allTransactions);
            model.addAttribute("currentPath", "/home");
            return "home";
    }
}
