package com.adso.thymeleaf.User;

import com.adso.thymeleaf.controller.UserController;
import com.adso.thymeleaf.model.User;
import com.adso.thymeleaf.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.adso.thymeleaf.utils.Constants.ACTION_ATTRIBUTE_USERS;
import static com.adso.thymeleaf.utils.Constants.ACTION_GET_ALL_USERS;
import static com.adso.thymeleaf.utils.Constants.ACTION_GET_USER;
import static com.adso.thymeleaf.utils.Constants.ACTION_UPDATE_USER;
import static com.adso.thymeleaf.utils.Constants.MESSAGE_CREATE_USER;
import static com.adso.thymeleaf.utils.Constants.MESSAGE_DELETE_USER;
import static com.adso.thymeleaf.utils.Constants.MODEL_ATTRIBUTE_ACTION;
import static com.adso.thymeleaf.utils.Constants.MODEL_ATTRIBUTE_MESSAGE;
import static com.adso.thymeleaf.utils.Constants.MODEL_ATTRIBUTE_SEARCHED;
import static com.adso.thymeleaf.utils.Constants.MODEL_ATTRIBUTE_USER;
import static com.adso.thymeleaf.utils.Constants.REDIRECT;
import static com.adso.thymeleaf.utils.Constants.USER_FORM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    @Mock
    private IUserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUserForm_WithGetAllUsersAction() {

        String action = ACTION_GET_ALL_USERS;

        User user1 = new User();
        user1.setId(1L);
        user1.setName("Alice");
        user1.setEmail("alice@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Bob");
        user2.setEmail("bob@example.com");

        List<User> fakeUsers = Arrays.asList(user1, user2);
        when(userService.getAllUsers()).thenReturn(fakeUsers);

        Model model = new ExtendedModelMap();

        String viewName = userController.userForm(action, model);

        assertEquals(USER_FORM, viewName, "El nombre de la vista debe ser USER_FORM");
        assertEquals(action, model.getAttribute(MODEL_ATTRIBUTE_ACTION), "El atributo de acción debe ser el recibido");
        assertEquals(fakeUsers, model.getAttribute(ACTION_ATTRIBUTE_USERS), "La lista de usuarios debe coincidir con la devuelta por el servicio");
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testUserForm_WithOtherAction() {
        String action = "otra_accion";
        Model model = new ExtendedModelMap();

        String viewName = userController.userForm(action, model);

        assertEquals(USER_FORM, viewName, "El nombre de la vista debe ser USER_FORM");
        assertEquals(action, model.getAttribute(MODEL_ATTRIBUTE_ACTION), "El atributo de acción debe ser el recibido");
        assertNull(model.getAttribute(ACTION_ATTRIBUTE_USERS), "No debe existir atributo de usuarios para una acción diferente");
        verify(userService, never()).getAllUsers();
    }

    @Test
    void testGetUserById_WithDefaultAction() {

        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("Charlie");
        user.setEmail("charlie@example.com");
        when(userService.getUserById(userId)).thenReturn(user);

        Model model = new ExtendedModelMap();

        String action = null;

        String viewName = userController.getUserById(userId, action, model);

        assertEquals(USER_FORM, viewName, "El nombre de la vista debe ser USER_FORM");
        assertEquals(user, model.getAttribute(MODEL_ATTRIBUTE_USER), "El usuario debe ser el devuelto por el servicio");
        assertEquals(true, model.getAttribute(MODEL_ATTRIBUTE_SEARCHED), "El atributo 'searched' debe ser true");
        assertEquals(ACTION_GET_USER, model.getAttribute(MODEL_ATTRIBUTE_ACTION), "La acción debe ser ACTION_GET_USER");
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void testGetUserById_WithUpdateAction() {

        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("Charlie");
        user.setEmail("charlie@example.com");
        when(userService.getUserById(userId)).thenReturn(user);

        Model model = new ExtendedModelMap();

        String action = ACTION_UPDATE_USER;

        String viewName = userController.getUserById(userId, action, model);

        assertEquals(USER_FORM, viewName, "El nombre de la vista debe ser USER_FORM");
        assertEquals(user, model.getAttribute(MODEL_ATTRIBUTE_USER), "El usuario debe ser el devuelto por el servicio");
        assertEquals(true, model.getAttribute(MODEL_ATTRIBUTE_SEARCHED), "El atributo 'searched' debe ser true");
        assertEquals(ACTION_UPDATE_USER, model.getAttribute(MODEL_ATTRIBUTE_ACTION), "La acción debe ser ACTION_UPDATE_USER");
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void testSaveUser() {

        User user = new User();
        user.setId(1L);
        user.setName("Eva");
        user.setEmail("eva@example.com");
        when(userService.saveUser(user)).thenReturn("User saved successfully");

        Model model = new ExtendedModelMap();

        String viewName = userController.saveUser(user, model);

        assertEquals(REDIRECT, viewName, "La respuesta debe ser REDIRECT");
        assertEquals(MESSAGE_CREATE_USER, model.getAttribute(MODEL_ATTRIBUTE_MESSAGE), "El mensaje debe ser MESSAGE_CREATE_USER");
        verify(userService, times(1)).saveUser(user);
    }

    @Test
    void deleteUser_UserExists_ShouldReturnRedirect() {
        Long userId = 1L;
        when(userService.deleteUser(userId)).thenReturn("User deleted successfully");

        Model model = new ExtendedModelMap();

        String viewName = userController.deleteUser(userId, model);

        assertEquals(REDIRECT, viewName, "La respuesta debe ser USER_FORM");
        assertEquals("User deleted successfully", model.getAttribute(MODEL_ATTRIBUTE_MESSAGE), "El mensaje debe ser 'User deleted successfully'");
        assertEquals(true, model.getAttribute(MODEL_ATTRIBUTE_SEARCHED), "El atributo 'searched' debe ser true");
        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void deleteUser_UserDoesNotExist_ShouldReturnUserFormWithErrorMessage() {
        Long userId = 1L;
        when(userService.deleteUser(userId)).thenReturn("User not found");

        Model model = new ExtendedModelMap();

        String viewName = userController.deleteUser(userId, model);

        assertEquals(REDIRECT, viewName, "La respuesta debe ser USER_FORM");
        assertEquals("User not found", model.getAttribute(MODEL_ATTRIBUTE_MESSAGE), "El mensaje debe ser 'User not found'");
        assertEquals(true, model.getAttribute(MODEL_ATTRIBUTE_SEARCHED), "El atributo 'searched' debe ser true");
        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void testUpdateUser() {

        User user = new User();
        user.setId(1L);
        user.setName("Frank");
        user.setEmail("frank@example.com");
        when(userService.updateUser(user)).thenReturn("User updated successfully");

        Model model = new ExtendedModelMap();

        String viewName = userController.updateUser(user, model);

        assertEquals(REDIRECT, viewName, "La respuesta debe ser REDIRECT");
        assertEquals(user, model.getAttribute(MODEL_ATTRIBUTE_USER), "El usuario en el modelo debe ser el mismo que se actualizó");
        assertEquals(true, model.getAttribute(MODEL_ATTRIBUTE_SEARCHED), "El atributo 'searched' debe ser true");
        assertEquals(ACTION_UPDATE_USER, model.getAttribute(MODEL_ATTRIBUTE_ACTION), "La acción debe ser ACTION_UPDATE_USER");
        verify(userService, times(1)).updateUser(user);
    }
}
