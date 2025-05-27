package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.exception.ServiceException;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.model.UserConnection;
import com.openclassrooms.paymybuddy.repository.UserConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserConnectionService {

    @Autowired
    private UserConnectionRepository userConnectionRepository;

    @Autowired
    private UserService userService;

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

    public List<UserConnection> getUser1Connection(User user1) {
        try {
            return userConnectionRepository.findByUser1(user1);
        } catch (Exception e) {
            throw new ServiceException("Échec lors de la récupération des relations", e);
        }
    }

    public Optional<UserConnection> getUserConnectionByUsers(User user1, User user2) {
        try {
            return userConnectionRepository.findByUser1AndUser2(user1, user2);
        } catch (Exception e) {
            throw new ServiceException("Échec lors de la récupération des relations", e);
        }
    }

    public User verifyUserByEmail(String email) {
        return userService.getUserByEmail(email)
                .orElseThrow(() -> new ServiceException("Utilisateur non trouvé avec l'email: " + email));
    }


    public void deleteUserConnection(UserConnection userConnection) {
        try {
            userConnectionRepository.delete(userConnection);
        } catch (Exception e) {
            throw new ServiceException("Échec de suppression de compte", e);
        }
    }
}
