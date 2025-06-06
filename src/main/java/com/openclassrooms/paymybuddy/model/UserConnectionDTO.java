package com.openclassrooms.paymybuddy.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object for User Connection.
 * Represents a connection between two users.
 */
@Data
@AllArgsConstructor
public class UserConnectionDTO {
    private UserDTO user1;
    private UserDTO user2;
}
