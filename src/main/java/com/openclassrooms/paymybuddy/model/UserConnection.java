package com.openclassrooms.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * User connection/relation model
 */
@Entity
@Table(name = "User_Connection")
@Data
public class UserConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user1;


    @ManyToOne
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;
}
