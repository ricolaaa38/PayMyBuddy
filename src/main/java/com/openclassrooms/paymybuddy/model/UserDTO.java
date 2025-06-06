package com.openclassrooms.paymybuddy.model;

import lombok.Data;

/**
 * Data Transfer Object for User.
 */
@Data
public class UserDTO {
    private Integer id;
    private String email;
    private String username;
    private double solde;
}
