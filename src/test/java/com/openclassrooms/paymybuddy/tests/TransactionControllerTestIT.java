package com.openclassrooms.paymybuddy.tests;

import com.openclassrooms.paymybuddy.config.TestSecurityConfig;
import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
public class TransactionControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    private User sender;
    private User receiver;
    private Transaction testTransaction;

    @BeforeEach
    public void setUp() {
        transactionRepository.deleteAll();
        userRepository.deleteAll();

        sender = new User();
        sender.setUsername("sender");
        sender.setEmail("sender@example.com");
        sender.setPassword("password");
        sender.setSolde(150.0);
        sender = userService.saveUser(sender);

        receiver = new User();
        receiver.setUsername("receiver");
        receiver.setEmail("receiver@example.com");
        receiver.setPassword("password");
        receiver.setSolde(50.0);
        receiver = userService.saveUser(receiver);

        testTransaction = new Transaction();
        testTransaction.setSender(sender);
        testTransaction.setReceiver(receiver);
        testTransaction.setDescription("Test Transaction");
        testTransaction.setAmount(100.0);
    }

    @Test
    public void testCreateTransaction() throws Exception {
        mockMvc.perform(post("/api/transactions/create")
                        .param("senderId", String.valueOf(sender.getId()))
                        .param("receiverId", String.valueOf(receiver.getId()))
                        .param("description", "Test Transaction")
                        .param("amount", "100.0"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testGetTransactionsBySender() throws Exception {
        transactionService.saveTransaction(testTransaction);

        mockMvc.perform(get("/api/transactions/sender")
                        .param("senderId", String.valueOf(sender.getId())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetTransactionsByReceiver() throws Exception {
        transactionService.saveTransaction(testTransaction);

        mockMvc.perform(get("/api/transactions/receiver")
                        .param("receiverId", String.valueOf(receiver.getId())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}