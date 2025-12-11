package org.example.springdemoweek2.service;

import org.example.springdemoweek2.model.User;
import org.example.springdemoweek2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(Long id, String name) {
        return userRepository.saveUser(id, name);
    }

    public User getUser(Long id) {
        return userRepository.findUserById(id);
    }
}