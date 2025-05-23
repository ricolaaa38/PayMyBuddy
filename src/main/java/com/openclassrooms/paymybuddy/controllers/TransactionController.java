package com.openclassrooms.paymybuddy.controllers;

import com.openclassrooms.paymybuddy.exception.ControllerException;
import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public String createTransaction(@RequestParam int senderId,
                                    @RequestParam int receiverId,
                                    @RequestParam String description,
                                    @RequestParam double amount,
                                    RedirectAttributes redirectAttributes) {
        try {

            User sender = userService.getUserById(senderId)
                    .orElseThrow(() -> new ControllerException("Expéditeur introuvable avec l'ID: " + senderId));
            User receiver = userService.getUserById(receiverId)
                    .orElseThrow(() -> new ControllerException("Destinataire introuvable avec l'ID: " + receiverId));

            Transaction transaction = new Transaction();
            transaction.setSender(sender);
            transaction.setReceiver(receiver);
            transaction.setDescription(description);
            transaction.setAmount(amount);

            transactionService.saveTransaction(transaction);
            redirectAttributes.addFlashAttribute("success", "Transaction envoyée avec succès !");
            return "redirect:/home";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'envoi de la transaction: " + e.getMessage());
            return "redirect:/home";
        }
    }

    @GetMapping("/sender")
    public ResponseEntity<List<Transaction>> getTransactionsBySender(@RequestParam int senderId) {
        User sender = userService.getUserById(senderId).orElseThrow(() -> new ControllerException("Sender not found with id: " + senderId));
        List<Transaction> transactions = transactionService.getTransactionBySender(sender);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/receiver")
    public ResponseEntity<List<Transaction>> getTransactionsByReceiver(@RequestParam int receiverId) {
        User receiver = userService.getUserById(receiverId).orElseThrow(() -> new ControllerException("Receiver not found with id: " + receiverId));
        List<Transaction> transactions = transactionService.getTransactionByReceiver(receiver);
        return ResponseEntity.ok(transactions);
    }
}
