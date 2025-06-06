package com.openclassrooms.paymybuddy.tests;

import com.openclassrooms.paymybuddy.exception.ServiceException;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.security.PasswordEncoderService;
import com.openclassrooms.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoderService passwordEncoderService;

    private User user;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setPassword("plainPassword");
        user.setUsername("testuser");
        user.setSolde(100.0);
    }

    @Test
    void testGetUsers_shouldReturnAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        Iterable<User> result = userService.getUsers();
        assertThat(result).contains(user);
    }

    @Test
    void testGetUserById_shouldReturnUser() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        Optional<User> result = userService.getUserById(1);
        assertThat(result).contains(user);
    }


    @Test
    void testGetUserByEmail_shouldReturnUser() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        Optional<User> result = userService.getUserByEmail("test@example.com");
        assertThat(result).contains(user);
    }


    @Test
    void testSaveUser_shouldEncodePasswordAndSaveUser() {
        when(passwordEncoderService.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.saveUser(user);

        assertThat(result).isEqualTo(user);
        assertThat(user.getPassword()).isEqualTo("encodedPassword");
        verify(userRepository).save(user);
    }


    @Test
    void testComparePasswords_shouldReturnTrue() {
        when(passwordEncoderService.matches("raw", "encoded")).thenReturn(true);
        assertThat(userService.comparePasswords("raw", "encoded")).isTrue();
    }

    @Test
    void testUpdateUser_shouldModifyFieldsAndSave() {
        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setEmail("new@example.com");
        updatedUser.setUsername("newUser");
        updatedUser.setSolde(200.0);
        updatedUser.setPassword("newPassword");

        when(passwordEncoderService.matches(any(), any())).thenReturn(false);
        when(passwordEncoderService.encode("newPassword")).thenReturn("encodedNew");
        when(userRepository.save(any())).thenReturn(updatedUser);

        User result = userService.updateUser(updatedUser, user);

        assertThat(result).isNotNull();
        assertThat(user.getEmail()).isEqualTo("new@example.com");
        assertThat(user.getUsername()).isEqualTo("newUser");
        assertThat(user.getSolde()).isEqualTo(200.0);
        assertThat(user.getPassword()).isEqualTo("encodedNew");
    }


    @Test
    void testDeleteUser_shouldDeleteUser() {
        userService.deleteUser(user);
        verify(userRepository).delete(user);
    }

}
