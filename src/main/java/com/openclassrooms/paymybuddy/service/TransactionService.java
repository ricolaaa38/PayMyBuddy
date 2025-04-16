package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.exception.ServiceException;
import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction saveTransaction(Transaction transaction) {
       try  {
           return transactionRepository.save(transaction);
       } catch (Exception e) {
           throw new ServiceException("Failed to save transaction", e);
       }

    }

    public List<Transaction> getTransactionBySender(User sender) {
        try {
            return transactionRepository.findBySender(sender);
        } catch (Exception e) {
            throw new ServiceException("Failed to retrieve transactions by sender", e);
        }
    }

    public List<Transaction> getTransactionByReceiver(User receiver) {
        try {
            return transactionRepository.findByReceiver(receiver);
        } catch (Exception e) {
            throw new ServiceException("Failed to retrieve transactions by receiver", e);
        }
    }
}
