package com.openclassrooms.paymybuddy.tests;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.model.UserConnection;
import com.openclassrooms.paymybuddy.repository.UserConnectionRepository;
import com.openclassrooms.paymybuddy.service.UserConnectionService;
import com.openclassrooms.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class UserConnectionServiceTest {

    @Mock
    private UserConnectionRepository userConnectionRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserConnectionService userConnectionService;

    private User user1;
    private User user2;
    private UserConnection userConnection;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user1 = new User();
        user1.setId(1);
        user1.setEmail("user1@example.com");

        user2 = new User();
        user2.setId(2);
        user2.setEmail("user2@example.com");

        userConnection = new UserConnection();
        userConnection.setUser1(user1);
        userConnection.setUser2(user2);
    }

    @Test
    void saveUserConnection_shouldReturnSavedConnection() {
        when(userConnectionRepository.findByUser1AndUser2(user1, user2)).thenReturn(Optional.empty());
        when(userConnectionRepository.save(userConnection)).thenReturn(userConnection);

        UserConnection result = userConnectionService.saveUserConnection(userConnection);

        assertThat(result).isEqualTo(userConnection);
        verify(userConnectionRepository).save(userConnection);
    }

    @Test
    void getUser1Connection_shouldReturnConnectionList() {
        List<UserConnection> connections = List.of(userConnection);
        when(userConnectionRepository.findByUser1(user1)).thenReturn(connections);

        List<UserConnection> result = userConnectionService.getUser1Connection(user1);

        assertThat(result).containsExactly(userConnection);
        verify(userConnectionRepository).findByUser1(user1);
    }

    @Test
    void getUserConnectionByUsers_shouldReturnConnection() {
        when(userConnectionRepository.findByUser1AndUser2(user1, user2)).thenReturn(Optional.of(userConnection));

        Optional<UserConnection> result = userConnectionService.getUserConnectionByUsers(user1, user2);

        assertThat(result).isPresent().contains(userConnection);
        verify(userConnectionRepository).findByUser1AndUser2(user1, user2);
    }

    @Test
    void verifyUserByEmail_shouldReturnUser() {
        when(userService.getUserByEmail("user1@example.com")).thenReturn(Optional.of(user1));

        User result = userConnectionService.verifyUserByEmail("user1@example.com");

        assertThat(result).isEqualTo(user1);
        verify(userService).getUserByEmail("user1@example.com");
    }

    @Test
    void deleteUserConnection_shouldCallRepositoryDelete() {
        userConnectionService.deleteUserConnection(userConnection);
    }
}