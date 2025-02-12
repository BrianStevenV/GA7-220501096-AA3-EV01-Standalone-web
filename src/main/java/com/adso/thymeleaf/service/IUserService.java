package com.adso.thymeleaf.service;

import com.adso.thymeleaf.model.User;

import java.util.List;

public interface IUserService {

    User getUserById(Long id);
    List<User> getAllUsers();
    String saveUser(User user);
    String deleteUser(Long id);
    String updateUser(User user);

}
