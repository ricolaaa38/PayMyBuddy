package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.model.UserConnection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserConnectionRepository extends CrudRepository<UserConnection, Integer> {
    List<UserConnection> findByUser1(User user1);
    Optional<UserConnection> findByUser1AndUser2(User user1, User user2);
}