package com.openclassrooms.paymybuddy.controllers;

import com.openclassrooms.paymybuddy.exception.ControllerException;
import com.openclassrooms.paymybuddy.model.*;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controllers for managing transactions between users.
 */
@Controller
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    /**
     * Creates a new transaction between two users.
     *
     * @param senderId        the ID of the sender
     * @param receiverId      the ID of the receiver
     * @param description     the description of the transaction
     * @param amount          the amount of money to be transferred
     * @param redirectAttributes attributes for redirecting with messages
     * @return a redirect to the home page with success or error message
     */
    @PostMapping("/create")
    public String createTransaction(@RequestParam int senderId,
                                    @RequestParam int receiverId,
                                    @RequestParam String description,
                                    @RequestParam double amount,
                                    RedirectAttributes redirectAttributes) {
        try {
            Transaction transaction = getTransaction(senderId, receiverId, description, amount);
            transactionService.saveTransaction(transaction);
            redirectAttributes.addFlashAttribute("success", "Transaction envoyée avec succès !");
            return "redirect:/home";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'envoi de la transaction: " + e.getMessage());
            return "redirect:/home";
        }
    }

    /**
     * Creates a transaction object with the given parameters.
     *
     * @param senderId    the ID of the sender
     * @param receiverId  the ID of the receiver
     * @param description the description of the transaction
     * @param amount      the amount of money to be transferred
     * @return a Transaction object
     */
    private Transaction getTransaction(int senderId, int receiverId, String description, double amount) {
        User sender = userService.getUserById(senderId)
                .orElseThrow(() -> new ControllerException("Expéditeur introuvable avec l'ID: " + senderId));
        User receiver = userService.getUserById(receiverId)
                .orElseThrow(() -> new ControllerException("Destinataire introuvable avec l'ID: " + receiverId));

        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setDescription(description);
        transaction.setAmount(amount);
        return transaction;
    }

    /**
     * Retrieves all transactions made by a specific user.
     *
     * @param senderId the ID of the user whose transactions are to be retrieved
     * @return a list of transactions made by the user
     */
    @GetMapping("/sender")
    public ResponseEntity<List<TransactionDTO>> getTransactionsBySender(@RequestParam int senderId) {
        User sender = userService.getUserById(senderId).orElseThrow(() -> new ControllerException("Sender not found with id: " + senderId));
        List<Transaction> transactions = transactionService.getTransactionBySender(sender);
        List<TransactionDTO> transactionDTOs = transactions.stream()
                .map(TransactionMapper::toDTO)
                .toList();

        return ResponseEntity.ok(transactionDTOs);
    }

    /**
     * Retrieves all transactions received by a specific user.
     *
     * @param receiverId the ID of the user whose received transactions are to be retrieved
     * @return a list of transactions received by the user
     */
    @GetMapping("/receiver")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByReceiver(@RequestParam int receiverId) {
        User receiver = userService.getUserById(receiverId).orElseThrow(() -> new ControllerException("Receiver not found with id: " + receiverId));
        List<Transaction> transactions = transactionService.getTransactionByReceiver(receiver);
        List<TransactionDTO> transactionDTOs = transactions.stream()
                .map(TransactionMapper::toDTO)
                .toList();

        return ResponseEntity.ok(transactionDTOs);
    }
}
