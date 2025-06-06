package com.openclassrooms.paymybuddy.tests;

import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransactionService transactionService;

    private User sender;
    private User receiver;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sender = new User();
        sender.setId(1);
        sender.setEmail("sender@example.com");
        sender.setSolde(100.0);

        receiver = new User();
        receiver.setId(2);
        receiver.setEmail("receiver@example.com");
        receiver.setSolde(50.0);

        transaction = new Transaction();
        transaction.setId(1);
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(25.0);
        transaction.setDate(LocalDateTime.now());
    }

    @Test
    void saveTransaction_shouldUpdateBalancesAndSaveTransaction() {
        when(userRepository.save(sender)).thenReturn(sender);
        when(userRepository.save(receiver)).thenReturn(receiver);
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        Transaction result = transactionService.saveTransaction(transaction);

        assertThat(result).isEqualTo(transaction);
        assertThat(sender.getSolde()).isEqualTo(75.0);
        assertThat(receiver.getSolde()).isEqualTo(75.0);
        verify(userRepository).save(sender);
        verify(userRepository).save(receiver);
        verify(transactionRepository).save(transaction);
    }

    @Test
    void getTransactionBySender_shouldReturnTransactions() {
        List<Transaction> transactions = List.of(transaction);
        when(transactionRepository.findBySenderOrderByDateDesc(sender)).thenReturn(transactions);

        List<Transaction> result = transactionService.getTransactionBySender(sender);

        assertThat(result).containsExactly(transaction);
        verify(transactionRepository).findBySenderOrderByDateDesc(sender);
    }

    @Test
    void getTransactionByReceiver_shouldReturnTransactions() {
        List<Transaction> transactions = List.of(transaction);
        when(transactionRepository.findByReceiverOrderByDateDesc(receiver)).thenReturn(transactions);

        List<Transaction> result = transactionService.getTransactionByReceiver(receiver);

        assertThat(result).containsExactly(transaction);
        verify(transactionRepository).findByReceiverOrderByDateDesc(receiver);
    }
}

