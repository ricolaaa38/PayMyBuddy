package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.exception.ServiceException;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.security.PasswordEncoderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoderService passwordEncoderService;


    public Iterable<User> getUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException("No user was found", e);
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

    public boolean authenticateUser(String email, String password) {
        return getUserByEmail(email)
                .map(user -> {
                    if (passwordEncoderService.matches(password, user.getPassword())) {
                        return true;
                    } else {
                        throw new ServiceException("Invalid password for user: " + email);
                    }
                })
                .orElseThrow(() -> new ServiceException("User not found with email: " + email));
    }
}

//    Function<Integer, Integer> f = new Function<Integer, Integer>() {
//        @Override
//        public Integer apply(Integer integer) {
//            return integer * 2;
//        }
//    };
