package com.adso.thymeleaf.User;

import com.adso.thymeleaf.model.User;
import com.adso.thymeleaf.repository.IUserRepository;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestPropertySource(locations = "classpath:application-dev.yml")
@SpringBootTest
public class UserServiceJpaTest {
    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserServiceJpa userServiceJpa;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
    }

    @Test
    void testFindById_UserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userServiceJpa.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<User> result = userServiceJpa.findById(1L);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveUser() {
        userServiceJpa.saveUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(user, new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userServiceJpa.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);
        userServiceJpa.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testExistsById() {
        when(userRepository.existsById(1L)).thenReturn(true);
        assertTrue(userServiceJpa.existsById(1L));
        verify(userRepository, times(1)).existsById(1L);
    }

    @Test
    void testExistsByEmail() {
        when(userRepository.existsByEmail("john.doe@example.com")).thenReturn(true);
        assertTrue(userServiceJpa.existsByEmail("john.doe@example.com"));
        verify(userRepository, times(1)).existsByEmail("john.doe@example.com");
    }
}
