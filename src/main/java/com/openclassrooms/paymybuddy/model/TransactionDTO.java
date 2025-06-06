package com.openclassrooms.paymybuddy.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Transaction.
 */
@Data
public class TransactionDTO {
    private UserDTO sender;
    private UserDTO receiver;
    private String description;
    private double amount;
    private LocalDateTime date;
}
