package com.adso.thymeleaf.service.jpa;

import com.adso.thymeleaf.model.User;
import com.adso.thymeleaf.repository.IUserRepository;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Transactional(readOnly = true)
public class UserServiceJpa {
    private final IUserRepository userRepository;

    public UserServiceJpa(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findById(Long id){
        return Optional.ofNullable(userRepository.findById(id).orElse(null));
    }

    @Transactional
    public void saveUser(User user){
        userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    @Transactional
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
