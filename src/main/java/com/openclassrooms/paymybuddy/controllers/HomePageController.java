package com.openclassrooms.paymybuddy.controllers;

import com.openclassrooms.paymybuddy.model.*;
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

/**
 * Controller that handle the home page.
 */
@Controller
public class HomePageController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserConnectionService userConnectionService;

    @Autowired
    private TransactionService transactionService;

    /**
     * Displays the home page with user details, connections, and transactions.
     *
     * @param currentUserDetails the currently authenticated user details
     * @param model              the model to add attributes for the view
     * @return the name of the home view
     */
    @GetMapping("/home")
    public String homePage(@AuthenticationPrincipal CustomUserDetails currentUserDetails, Model model) {
            if (currentUserDetails == null) {
                throw new RuntimeException("Utilisateur non authentifié");
            }

            User user = userService.getUserById(currentUserDetails.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

            model.addAttribute("user", UserMapper.toDTO(user));

            List<UserConnectionDTO> connections = userConnectionService.getUser1Connection(user).stream()
                .map(connection -> new UserConnectionDTO(
                        UserMapper.toDTO(connection.getUser1()),
                        UserMapper.toDTO(connection.getUser2())
                ))
                .toList();

            List<TransactionDTO> sentTransactions = transactionService.getTransactionBySender(user).stream()
                .map(TransactionMapper::toDTO)
                .toList();
            List<TransactionDTO> receivedTransactions = transactionService.getTransactionByReceiver(user).stream()
                .map(TransactionMapper::toDTO)
                .toList();

            List<TransactionDTO> allTransactions = new java.util.ArrayList<>();
            allTransactions.addAll(sentTransactions);
            allTransactions.addAll(receivedTransactions);
            allTransactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));

            model.addAttribute("user", UserMapper.toDTO(user));
            model.addAttribute("connections", connections);
            model.addAttribute("transactions", allTransactions);
            model.addAttribute("currentPath", "/home");
            return "home";
    }
}
