//package org.example.springdemoweek2.repository;
//
//import org.example.springdemoweek2.model.User;
//import org.springframework.stereotype.Repository;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Repository
//public class UserRepository {
//
//    private Map<Long, User> userDB = new ConcurrentHashMap<>();
//
//    public User saveUser(Long id, String name) {
//        User user = new User(id, name);
//        userDB.put(id, user);
//        return user;
//    }
//
//    public User findUserById(Long id) {
//        return userDB.get(id);
//    }
//}


//adding actual JPA repo
package org.example.springdemoweek2.repository;

import org.example.springdemoweek2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    //Derived Queries
    //WHERE name = ____
    List<User> findByName(String name);
    //WHERE name LIKE %___%
    List<User> findByNameContaining(String keyword);
}
//automatically has : save(), findById(), etc by default.
