package org.example.springdemoweek2.repository;

import org.example.springdemoweek2.model.User;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepository {

    private Map<Long, User> userDB = new ConcurrentHashMap<>();

    public User saveUser(Long id, String name) {
        User user = new User(id, name);
        userDB.put(id, user);
        return user;
    }

    public User findUserById(Long id) {
        return userDB.get(id);
    }
}