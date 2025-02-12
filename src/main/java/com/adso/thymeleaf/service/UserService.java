package com.adso.thymeleaf.service;

import com.adso.thymeleaf.model.User;
import com.adso.thymeleaf.repository.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public String saveUser(User user) {
        userRepository.save(user);
        return "User saved successfully";
    }

    @Override
    @Transactional
    public String deleteUser(Long id) {
        userRepository.deleteById(id);
        return "User deleted successfully";
    }

    @Override
    @Transactional
    public String updateUser(User user) {
        Optional<User> update = userRepository.findById(user.getId());

        if (update.isPresent()) {
            User existingUser = update.get();
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            userRepository.save(existingUser);
            return "User updated successfully";
        } else {
            return "User not found";
        }
    }

}
