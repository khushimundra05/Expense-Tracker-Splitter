package org.example.springdemoweek2.service;

import org.example.springdemoweek2.model.User;
import org.example.springdemoweek2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.springdemoweek2.exception.UserNotFoundException;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    //Create
    public User createUser(Long id, String name) {
        User user = new User(id,name);
        return userRepository.save(user); //.save() is a function of JPA repository
    }

    //Read
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("User not found"));
    } //findById() and orElseThrow() are also inbuilt with JPA

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //Update
    public User updateUser(Long id, String newName) {
        User user = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("User not found"));
        user.setName(newName);
        return userRepository.save(user);
    }

    //Delete
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

}