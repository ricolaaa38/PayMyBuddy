//package com.openclassrooms.paymybuddy;
//
//import com.openclassrooms.paymybuddy.controllers.UserController;
//import com.openclassrooms.paymybuddy.model.User;
//import com.openclassrooms.paymybuddy.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//public class UserControllerTest {
//
//    @Mock
//    private UserService userService;
//
//    @InjectMocks
//    private UserController userController;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    //@Test
//    public void testRegisterUser() {
//        User user = new User();
//        user.setEmail("test@example.com");
//        user.setUsername("testName");
//        user.setPassword("password");
//
//        when(userService.saveUser(any(User.class))).thenReturn(user);
//
//        ResponseEntity<User> response = userController.registerUser(user);
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertEquals(user, response.getBody());
//
//    }
//
//
//    //@Test
////    public void testLoginUserSuccess() {
////        String email = "test@example.com";
////        String password = "password";
////
////        when(userService.authenticateUser(anyString(), anyString())).thenReturn(true);
////
////        ResponseEntity<String> response = userController.loginUser(email, password);
////
////        assertEquals(HttpStatus.OK, response.getStatusCode());
////        assertEquals("Login successful", response.getBody());
////    }
//
//    @Test
//    public void testFindUserByEmail() {
//        User user = new User();
//        user.setEmail("test@example.com");
//
//        when(userService.getUserByEmail("test@example.com")).thenReturn(Optional.of(user));
//
//        ResponseEntity<User> response = userController.findUserByEmail("test@example.com");
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(user, response.getBody());
//
//
//    }
//
//   @Test
//    public void testFindUserByEmailNotFound() {
//        when(userService.getUserByEmail("nonexistent@example.com")).thenReturn(Optional.empty());
//
//        ResponseEntity<User> response = userController.findUserByEmail("nonexistent@example.com");
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//    }
//}