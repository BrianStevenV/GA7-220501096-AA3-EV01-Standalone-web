package com.adso.thymeleaf.controller;

import com.adso.thymeleaf.model.User;
import com.adso.thymeleaf.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.adso.thymeleaf.utils.Constants.ACTION_ATTRIBUTE_USERS;
import static com.adso.thymeleaf.utils.Constants.ACTION_GET_ALL_USERS;
import static com.adso.thymeleaf.utils.Constants.ACTION_GET_USER;
import static com.adso.thymeleaf.utils.Constants.ACTION_UPDATE_USER;
import static com.adso.thymeleaf.utils.Constants.MESSAGE_CREATE_USER;
import static com.adso.thymeleaf.utils.Constants.MESSAGE_USER_NOT_FOUND;
import static com.adso.thymeleaf.utils.Constants.MODEL_ATTRIBUTE_MESSAGE;
import static com.adso.thymeleaf.utils.Constants.MODEL_ATTRIBUTE_SEARCHED;
import static com.adso.thymeleaf.utils.Constants.REDIRECT;
import static com.adso.thymeleaf.utils.Constants.URI;
import static com.adso.thymeleaf.utils.Constants.GET_USER_FORM;
import static com.adso.thymeleaf.utils.Constants.MODEL_ATTRIBUTE_ACTION;
import static com.adso.thymeleaf.utils.Constants.MODEL_ATTRIBUTE_USER;
import static com.adso.thymeleaf.utils.Constants.USER_FORM;

@Controller
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping(GET_USER_FORM)
    public String userForm(@RequestParam String action, Model model) {
        model.addAttribute(MODEL_ATTRIBUTE_ACTION, action);

        if (ACTION_GET_ALL_USERS.equals(action)) {
            List<User> users = userService.getAllUsers();
            model.addAttribute(ACTION_ATTRIBUTE_USERS, users);
        }

        return USER_FORM;
    }

    @GetMapping(URI)
    public String getUserById(@RequestParam Long id, @RequestParam(required = false) String action,Model model) {
        User user = userService.getUserById(id);
        model.addAttribute(MODEL_ATTRIBUTE_USER, user);
        model.addAttribute(MODEL_ATTRIBUTE_SEARCHED, true);

        if(user == null) {
            model.addAttribute(MODEL_ATTRIBUTE_MESSAGE, MESSAGE_USER_NOT_FOUND);
        }
        if (ACTION_UPDATE_USER.equals(action)) {
            model.addAttribute(MODEL_ATTRIBUTE_ACTION, ACTION_UPDATE_USER);
        } else {
            model.addAttribute(MODEL_ATTRIBUTE_ACTION, ACTION_GET_USER);
        }

        return USER_FORM;
    }

    @PostMapping(URI)
    public String saveUser(@Valid @ModelAttribute User user, Model model) {
        userService.saveUser(user);
        model.addAttribute(MODEL_ATTRIBUTE_MESSAGE, MESSAGE_CREATE_USER);
        return REDIRECT;
    }

    @DeleteMapping(URI)
    public String deleteUser(@RequestParam Long id, Model model) {
        String message = userService.deleteUser(id);

        model.addAttribute(MODEL_ATTRIBUTE_MESSAGE, message);
        model.addAttribute(MODEL_ATTRIBUTE_SEARCHED, true);

        return REDIRECT;
    }

    @PutMapping(URI)
    public String updateUser(@Valid @ModelAttribute User user, Model model) {
        userService.updateUser(user);
        model.addAttribute(MODEL_ATTRIBUTE_USER, user);
        model.addAttribute(MODEL_ATTRIBUTE_SEARCHED, true);
        model.addAttribute(MODEL_ATTRIBUTE_ACTION, ACTION_UPDATE_USER);
        return REDIRECT;
    }
}
