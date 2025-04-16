package com.openclassrooms.paymybuddy;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User testUser;


    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        testUser = new User();
        testUser.setUsername("test user");
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("password");
    }

    @Test
    public void testRegisterUser() throws Exception {

        String jsonContent = objectMapper.writeValueAsString(testUser);
        System.out.println("JSON Content: " + jsonContent);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated());
    }

    @Test
    public void testFindUserByEmail() throws Exception {
        userService.saveUser(testUser);
        mockMvc.perform(get("/api/users/find")
                        .param("email", "testuser@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(testUser)));
    }

    @Test
    public void testFindUserByEmailNotFound() throws Exception {
        mockMvc.perform(get("/api/users/find")
                        .param("email", "nonexistent@example.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testLoginUserSuccess() throws Exception {

        userService.saveUser(testUser);

        Optional<User> savedUser = Optional.ofNullable(userRepository.findByEmail("testuser@example.com"));
        assertTrue(savedUser.isPresent());

        mockMvc.perform(post("/api/users/login")
                        .param("email", "testuser@example.com")
                        .param("password", "password"))
                .andExpect(status().isOk());
    }
}

