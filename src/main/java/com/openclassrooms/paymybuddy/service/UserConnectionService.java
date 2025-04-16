package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.exception.ServiceException;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.model.UserConnection;
import com.openclassrooms.paymybuddy.repository.UserConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserConnectionService {

    @Autowired
    private UserConnectionRepository userConnectionRepository;

    public UserConnection saveUserConnection(UserConnection userConnection) {
        try {
            return userConnectionRepository.save(userConnection);
        } catch (Exception e) {
            throw new ServiceException("Failed  to save connection between those Users", e);
        }
    }

    public List<UserConnection> getUser1Connection(User user1) {
        try {
            return userConnectionRepository.findByUser1(user1);
        } catch (Exception e) {
            throw new ServiceException("Failed to get the User connection's list", e);
        }
    }
}
