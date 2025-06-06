package com.openclassrooms.paymybuddy.model;

import com.openclassrooms.paymybuddy.model.UserDTO;
import com.openclassrooms.paymybuddy.model.User;

/**
 * Mapper class to convert User entities to UserDTOs.
 * This class provides a method to map User objects to UserDTO objects.
 */
public class UserMapper {

    public static UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());
        userDTO.setSolde(user.getSolde());
        return userDTO;
    }
}
