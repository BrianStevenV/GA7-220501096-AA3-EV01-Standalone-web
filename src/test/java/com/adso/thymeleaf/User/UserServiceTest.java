package com.adso.thymeleaf.User;

import com.adso.thymeleaf.model.User;
import com.adso.thymeleaf.service.UserService;
import com.adso.thymeleaf.service.jpa.UserServiceJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@TestPropertySource(locations = "classpath:application-dev.yml")
@SpringBootTest
public class UserServiceTest {
    @Mock
    private UserServiceJpa userServiceJpa;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
    }

    @Test
    void testGetUserById_UserExists() {
        when(userServiceJpa.findById(1L)).thenReturn(Optional.of(user));
        User result = userService.getUserById(1L);
        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(user, new User());
        when(userServiceJpa.getAllUsers()).thenReturn(users);
        List<User> result = userService.getAllUsers();
        assertEquals(2, result.size());
    }

    @Test
    void testSaveUser_UserAlreadyExists() {
        when(userServiceJpa.existsByEmail(user.getEmail())).thenReturn(true);
        String result = userService.saveUser(user);
        assertEquals("User already exists", result);
    }

    @Test
    void testSaveUser_Success() {
        when(userServiceJpa.existsByEmail(user.getEmail())).thenReturn(false);
        doNothing().when(userServiceJpa).saveUser(user);
        String result = userService.saveUser(user);
        assertEquals("User created successfully!", result);
    }

    @Test
    void testDeleteUser_UserExists() {
        when(userServiceJpa.existsById(1L)).thenReturn(true);
        doNothing().when(userServiceJpa).deleteUser(1L);
        String result = userService.deleteUser(1L);
        assertEquals("User deleted successfully", result);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userServiceJpa.existsById(1L)).thenReturn(false);
        String result = userService.deleteUser(1L);
        assertEquals("User not found", result);
    }

    @Test
    void testUpdateUser_UserExists() {
        when(userServiceJpa.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userServiceJpa).saveUser(any(User.class));
        String result = userService.updateUser(user);
        assertEquals("User updated successfully", result);
    }

    @Test
    void testUpdateUser_UserNotFound() {
        when(userServiceJpa.findById(1L)).thenReturn(Optional.empty());
        String result = userService.updateUser(user);
        assertEquals("User not found", result);
    }
}
