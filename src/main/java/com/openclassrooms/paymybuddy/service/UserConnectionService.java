package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.exception.ServiceException;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.model.UserConnection;
import com.openclassrooms.paymybuddy.repository.UserConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing the user connection/relation.
 */
@Service
public class UserConnectionService {

    @Autowired
    private UserConnectionRepository userConnectionRepository;

    @Autowired
    private UserService userService;

    /**
     * Saves a user connection if it does not already exist.
     *
     * @param userConnection the user connection to save
     * @return the saved user connection
     * @throws ServiceException if the connection already exists
     */
    public UserConnection saveUserConnection(UserConnection userConnection) {
        Optional<UserConnection> existingConnection = userConnectionRepository.findByUser1AndUser2(
                userConnection.getUser1(),
                userConnection.getUser2()
        );
        if (existingConnection.isPresent()) {
            throw new ServiceException("Cette relation existe déjà!");
        }
        return userConnectionRepository.save(userConnection);
    }

    /**
     * Retrieves all connections for a given user.
     *
     * @param user1 the user whose connections are to be retrieved
     * @return a list of user connections
     * @throws ServiceException if an error occurs during retrieval
     */
    public List<UserConnection> getUser1Connection(User user1) {
        try {
            return userConnectionRepository.findByUser1(user1);
        } catch (Exception e) {
            throw new ServiceException("Échec lors de la récupération des relations", e);
        }
    }

    /**
     * Retrieves a user connection by two users.
     *
     * @param user1 the first user
     * @param user2 the second user
     * @return an Optional containing the user connection if found, or empty if not found
     * @throws ServiceException if an error occurs during retrieval
     */
    public Optional<UserConnection> getUserConnectionByUsers(User user1, User user2) {
        try {
            return userConnectionRepository.findByUser1AndUser2(user1, user2);
        } catch (Exception e) {
            throw new ServiceException("Échec lors de la récupération des relations", e);
        }
    }

    /**
     * Verifies if a user exists by their email.
     *
     * @param email the email of the user to verify
     * @return the user if found
     * @throws ServiceException if the user is not found
     */
    public User verifyUserByEmail(String email) {
        return userService.getUserByEmail(email)
                .orElseThrow(() -> new ServiceException("Utilisateur non trouvé avec l'email: " + email));
    }


    /**
     * Deletes a user connection.
     *
     * @param userConnection the user connection to delete
     * @throws ServiceException if an error occurs during deletion
     */
    public void deleteUserConnection(UserConnection userConnection) {
        try {
            userConnectionRepository.delete(userConnection);
        } catch (Exception e) {
            throw new ServiceException("Échec de suppression de compte", e);
        }
    }
}
