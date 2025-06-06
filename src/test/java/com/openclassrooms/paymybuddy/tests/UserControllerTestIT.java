package com.openclassrooms.paymybuddy.tests;

import com.openclassrooms.paymybuddy.config.TestSecurityConfig;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
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
    public void testRegisterUserSuccess() throws Exception {
        mockMvc.perform(post("/api/users/register")
                        .param("email", testUser.getEmail())
                        .param("username", testUser.getUsername())
                        .param("password", testUser.getPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void testFindUserByEmail() throws Exception {
        userService.saveUser(testUser);

        mockMvc.perform(get("/api/users/find")
                        .param("email", testUser.getEmail()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("testuser@example.com"))
                .andExpect(jsonPath("$.username").value("test user"));
    }

    @Test
    public void testFindUserByEmailNotFound() throws Exception {
        mockMvc.perform(get("/api/users/find")
                        .param("email", "nonexistent@example.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testLoginUserFail() throws Exception {
        mockMvc.perform(post("/api/users/login")
                        .param("email", "wrong@example.com")
                        .param("password", "wrong"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void testUpdateUserRedirect() throws Exception {
        User saved = userService.saveUser(testUser);

        mockMvc.perform(post("/api/users/update")
                        .param("id", saved.getId().toString())
                        .param("email", "updated@example.com")
                        .param("username", "Updated User")
                        .param("password", "newpass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"));
    }

    @Test
    public void testDeleteUserSuccess() throws Exception {
        userService.saveUser(testUser);
        mockMvc.perform(delete("/api/users/delete")
                        .param("email", testUser.getEmail()))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }
}


