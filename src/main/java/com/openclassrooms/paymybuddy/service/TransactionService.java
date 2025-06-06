package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.exception.ServiceException;
import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *Service for managing transactions.
 */
@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Saves a transaction after validating the sender's balance.
     *
     * @param transaction the transaction to save
     * @return the saved transaction
     * @throws ServiceException if the sender's balance is insufficient or if an error occurs during saving
     */
    @Transactional
    public Transaction saveTransaction(Transaction transaction) {
        try {
            User sender = transaction.getSender();
            User receiver = transaction.getReceiver();
            double amount = transaction.getAmount();
            if (sender.getSolde() < amount) {
                throw new ServiceException("Solde insuffisant pour effectuer cette transaction.");
            }
            sender.setSolde(sender.getSolde() - amount);
            receiver.setSolde(receiver.getSolde() + amount);
            userRepository.save(sender);
            userRepository.save(receiver);
            return transactionRepository.save(transaction);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Une erreur est survenue lors de la sauvegarde de la transaction.", e);
        }
    }

    /**
     * Retrieves all transactions made by a specific sender.
     *
     * @param sender the user who sent the transactions
     * @return a list of transactions made by the sender
     * @throws ServiceException if an error occurs during retrieval
     */
    public List<Transaction> getTransactionBySender(User sender) {
        try {
            return transactionRepository.findBySenderOrderByDateDesc(sender);
        } catch (Exception e) {
            throw new ServiceException("Failed to retrieve transactions by sender", e);
        }
    }

    /**
     * Retrieves all transactions received by a specific user.
     *
     * @param receiver the user who received the transactions
     * @return a list of transactions received by the user
     * @throws ServiceException if an error occurs during retrieval
     */
    public List<Transaction> getTransactionByReceiver(User receiver) {
        try {
            return transactionRepository.findByReceiverOrderByDateDesc(receiver);
        } catch (Exception e) {
            throw new ServiceException("Failed to retrieve transactions by receiver", e);
        }
    }
}
