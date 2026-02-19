
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
