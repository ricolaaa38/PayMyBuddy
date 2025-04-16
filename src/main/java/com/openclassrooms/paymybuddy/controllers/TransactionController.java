package com.openclassrooms.paymybuddy.controllers;

import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction addTransaction = transactionService.saveTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(addTransaction);
    }

    @GetMapping("/sender")
    public ResponseEntity<List<Transaction>> getTransactionsBySender(@RequestParam int senderId) {
        User sender = userService.getUserById(senderId).orElseThrow(() -> new RuntimeException("Sender not found"));
        List<Transaction> transactions = transactionService.getTransactionBySender(sender);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/receiver")
    public ResponseEntity<List<Transaction>> getTransactionsByReceiver(@RequestParam int receiverId) {
        User receiver = userService.getUserById(receiverId).orElseThrow(() -> new RuntimeException("Receiver not found"));
        List<Transaction> transactions = transactionService.getTransactionByReceiver(receiver);
        return ResponseEntity.ok(transactions);
    }
}
