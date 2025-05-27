package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.exception.ServiceException;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.security.PasswordEncoderService;
import org.apache.catalina.util.StringUtil;
import org.apache.tomcat.util.buf.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoderService passwordEncoderService;

//    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public Iterable<User> getUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException("No user were found", e);
        }
    }

    public Optional<User> getUserById(Integer id) {
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            throw new ServiceException("User not found");
        }

    }

    public Optional<User> getUserByEmail(String email) {
        try {
            return Optional.ofNullable(userRepository.findByEmail(email));
        } catch (Exception e) {
            throw new ServiceException("Failed to retrieve user by email", e);
        }
    }

    public User saveUser(User user) {
        try {
            user.setPassword(passwordEncoderService.encode(user.getPassword()));
            return userRepository.save(user);
        } catch (Exception e) {
            throw new ServiceException("Failed to save user", e);
        }
    }

    public boolean comparePasswords(String rawPassword, String encodedPassword) {
        return passwordEncoderService.matches(rawPassword, encodedPassword);
    }

    public User updateUser(User updatedUser, User previousUserInfo ) {
        try {

//            logger.debug("Received updatedUser: {}", updatedUser);
//            logger.debug("Existing previousUserInfo: {}", previousUserInfo);

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

    public void deleteUser(User user) {
        try {
            userRepository.delete(user);
        } catch (Exception e) {
            throw new ServiceException("Failed to delete user", e);
        }
    }
}
