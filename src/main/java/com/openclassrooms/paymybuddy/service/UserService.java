package com.openclassrooms.paymybuddy.service;


import com.openclassrooms.paymybuddy.exception.ServiceException;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.security.PasswordEncoderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for managing the Users.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoderService passwordEncoderService;

    /**
     * Retrieves all users from the repository.
     *
     * @return an iterable of all users
     * @throws ServiceException if no users are found or if an error occurs during retrieval
     */
    public Iterable<User> getUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException("No user were found", e);
        }
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return an Optional containing the user if found, or empty if not found
     * @throws ServiceException if an error occurs during retrieval
     */
    public Optional<User> getUserById(Integer id) {
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            throw new ServiceException("User not found");
        }

    }

    /**
     * Retrieves a user by their email.
     *
     * @param email the email of the user to retrieve
     * @return an Optional containing the user if found, or empty if not found
     * @throws ServiceException if an error occurs during retrieval
     */
    public Optional<User> getUserByEmail(String email) {
        try {
            return Optional.ofNullable(userRepository.findByEmail(email));
        } catch (Exception e) {
            throw new ServiceException("Failed to retrieve user by email", e);
        }
    }

    /**
     * Saves a user to the repository.
     *
     * @param user the user to save
     * @return the saved user
     * @throws ServiceException if an error occurs during saving
     */
    public User saveUser(User user) {
        try {
            user.setPassword(passwordEncoderService.encode(user.getPassword()));
            return userRepository.save(user);
        } catch (Exception e) {
            throw new ServiceException("Failed to save user", e);
        }
    }

    /**
     * Compares a raw password with an encoded password.
     *
     * @param rawPassword the raw password to compare
     * @param encodedPassword the encoded password to compare against
     * @return true if the passwords match, false otherwise
     */
    public boolean comparePasswords(String rawPassword, String encodedPassword) {
        return passwordEncoderService.matches(rawPassword, encodedPassword);
    }

    /**
     * Updates a user's information.
     *
     * @param updatedUser the user with updated information
     * @param previousUserInfo the user with previous information
     * @return the updated user if any field was modified, null otherwise
     * @throws ServiceException if an error occurs during updating
     */
    public User updateUser(User updatedUser, User previousUserInfo ) {
        try {
            if (updatedUser.getId() == null) {
                throw new ServiceException("User ID is required for updating user information");
            }
            boolean isModified = false;

            if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty() && !updatedUser.getEmail().equals(previousUserInfo.getEmail())) {
                previousUserInfo.setEmail(updatedUser.getEmail());
                isModified = true;
            }
            if (updatedUser.getUsername() != null && !updatedUser.getUsername().isEmpty() && !updatedUser.getUsername().equals(previousUserInfo.getUsername())) {
                previousUserInfo.setUsername(updatedUser.getUsername());
                isModified = true;
            }
            if (updatedUser.getSolde() > 0 && updatedUser.getSolde() != previousUserInfo.getSolde()) {
                previousUserInfo.setSolde(updatedUser.getSolde());
                isModified = true;
            }
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty() && !passwordEncoderService.matches(updatedUser.getPassword(), previousUserInfo.getPassword())) {
                    previousUserInfo.setPassword(passwordEncoderService.encode(updatedUser.getPassword()));
                    isModified = true;
            }

            if (isModified) {
                return userRepository.save(previousUserInfo);
            } else {
               return null;
            }
        } catch (Exception e) {
            throw new ServiceException("Failed to update user", e);
        }
    }

    /**
     * Deletes a user from the repository.
     *
     * @param user the user to delete
     * @throws ServiceException if an error occurs during deletion
     */
    public void deleteUser(User user) {
        try {
            userRepository.delete(user);
        } catch (Exception e) {
            throw new ServiceException("Failed to delete user", e);
        }
    }
}
