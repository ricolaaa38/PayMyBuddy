package com.openclassrooms.paymybuddy;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserConnectionControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
        String jsonContent = objectMapper.writeValueAsString(testUserConnection);

        mockMvc.perform(post("/api/users-connections/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetUserConnection() throws Exception {
        userConnectionService.saveUserConnection(testUserConnection);

        mockMvc.perform(get("/api/users-connections/")
                .param("user1Id", String.valueOf(user1.getId())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
