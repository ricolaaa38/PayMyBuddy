package com.openclassrooms.paymybuddy.tests;

import com.openclassrooms.paymybuddy.config.TestSecurityConfig;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.model.UserConnection;
import com.openclassrooms.paymybuddy.repository.UserConnectionRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.service.UserConnectionService;
import com.openclassrooms.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
public class UserConnectionControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserConnectionService userConnectionService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserConnectionRepository userConnectionRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private UserConnection testUserConnection;

    @BeforeEach
    public void setUp() {
        userConnectionRepository.deleteAll();
        userRepository.deleteAll();

        user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        user1.setPassword("password");
        user1 = userService.saveUser(user1);

        user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setPassword("password");
        user2 = userService.saveUser(user2);

        testUserConnection = new UserConnection();
        testUserConnection.setUser1(user1);
        testUserConnection.setUser2(user2);
    }

    @Test
    public void testCreateUsersConnection() throws Exception {
        mockMvc.perform(post("/api/users-connections/create")
                        .param("user1id", String.valueOf(user1.getId()))
                        .param("user2Email", user2.getEmail())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testGetUserConnection() throws Exception {
        userConnectionService.saveUserConnection(testUserConnection);

        mockMvc.perform(get("/api/users-connections/")
                        .param("user1Id", String.valueOf(user1.getId())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testDeleteUserConnection() throws Exception {
        userConnectionService.saveUserConnection(testUserConnection);

        mockMvc.perform(delete("/api/users-connections/delete")
                        .param("user1Email", user1.getEmail())
                        .param("user2Email", user2.getEmail()))
                .andExpect(status().isOk())
                .andExpect(content().string("Connection successfully deleted"));
    }
}

