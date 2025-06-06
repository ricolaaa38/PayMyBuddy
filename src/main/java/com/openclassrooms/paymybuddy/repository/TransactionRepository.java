package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Transaction entities.
 * Provides methods to perform CRUD operations on Transaction data.
 */
@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
    List<Transaction> findBySender(User sender);
    List<Transaction> findByReceiver(User receiver);

    List<Transaction> findBySenderOrderByDateDesc(User sender);
    List<Transaction> findByReceiverOrderByDateDesc(User receiver);

}
