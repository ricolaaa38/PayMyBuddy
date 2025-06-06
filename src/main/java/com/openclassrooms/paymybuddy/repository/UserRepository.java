package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing User entities.
 * Provides methods to perform CRUD operations on User data.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);
    Optional<User> findOptionalByEmail(String email);
}
