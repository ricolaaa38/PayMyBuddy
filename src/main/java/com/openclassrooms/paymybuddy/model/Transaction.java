package com.openclassrooms.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "Transaction")
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(name = "description")
    private String description;

    @Column(nullable = false)
    private Double amount;
}
