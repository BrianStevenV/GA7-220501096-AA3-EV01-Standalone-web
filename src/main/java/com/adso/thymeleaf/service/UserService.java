package com.adso.thymeleaf.service;

import com.adso.thymeleaf.model.User;
import com.adso.thymeleaf.service.jpa.UserServiceJpa;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.adso.thymeleaf.utils.Constants.ERROR_MESSAGE_ACTION_DELETE;
import static com.adso.thymeleaf.utils.Constants.ERROR_MESSAGE_ACTION_SAVE;
import static com.adso.thymeleaf.utils.Constants.ERROR_MESSAGE_ACTION_UPDATE;
import static com.adso.thymeleaf.utils.Constants.MESSAGE_CREATE_USER;
import static com.adso.thymeleaf.utils.Constants.MESSAGE_USER_ALREADY_EXISTS;
import static com.adso.thymeleaf.utils.Constants.MESSAGE_USER_DELETED;
import static com.adso.thymeleaf.utils.Constants.MESSAGE_USER_NOT_FOUND;
import static com.adso.thymeleaf.utils.Constants.MESSAGE_USER_UPDATED;

@Service
@Transactional
public class UserService implements IUserService {

    private final UserServiceJpa userServiceJpa;

    public UserService(
            UserServiceJpa userServiceJpa
    ) {
        this.userServiceJpa = userServiceJpa;
    }

    @Override
    public User getUserById(Long id) {
        return userServiceJpa.findById(id).orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return userServiceJpa.getAllUsers();
    }

    @Override
    public String saveUser(User user) {
        try {
            if (userServiceJpa.existsByEmail(user.getEmail())) {
                return MESSAGE_USER_ALREADY_EXISTS;
            }
            userServiceJpa.saveUser(user);
            return MESSAGE_CREATE_USER;
        } catch (Exception e) {
            return ERROR_MESSAGE_ACTION_SAVE + e.getMessage();
        }
    }

    @Override
    public String deleteUser(Long id) {
        try {
            if (userServiceJpa.existsById(id)) {
                userServiceJpa.deleteUser(id);
                return MESSAGE_USER_DELETED;
            }
            return MESSAGE_USER_NOT_FOUND;
        } catch (Exception e) {
            return ERROR_MESSAGE_ACTION_DELETE + e.getMessage();
        }
    }

    @Override
    public String updateUser(User user) {
        try {
            Optional<User> existingUser = userServiceJpa.findById(user.getId());

            if (existingUser.isPresent()) {
                User data = existingUser.get();
                data.setName(user.getName());
                data.setEmail(user.getEmail());

                userServiceJpa.saveUser(data);
                return MESSAGE_USER_UPDATED;
            }
            return MESSAGE_USER_NOT_FOUND;
        } catch (Exception e) {
            return ERROR_MESSAGE_ACTION_UPDATE + e.getMessage();
        }
    }

}
