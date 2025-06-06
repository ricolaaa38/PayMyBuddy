package com.openclassrooms.paymybuddy.model;

import com.openclassrooms.paymybuddy.model.*;

/**
 * Mapper class to convert Transaction entities to TransactionDTOs.
 */
public class TransactionMapper {

    public static TransactionDTO toDTO(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSender(UserMapper.toDTO(transaction.getSender()));
        transactionDTO.setReceiver(UserMapper.toDTO(transaction.getReceiver()));
        transactionDTO.setDescription(transaction.getDescription());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setDate(transaction.getDate());

        return transactionDTO;
    }
}
