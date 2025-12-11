package org.example.springdemoweek2.controller;
import org.example.springdemoweek2.model.User;
import org.example.springdemoweek2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    @Autowired
    private UserService userService;

    @GetMapping("/test")
    public String test() {
        return "Controller loaded ✔";
    }


    @GetMapping("/hello")
    public String sayHello() {
        return "Hello Khushi! Your Spring Boot API is working 🚀";
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping("/user")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user.getId(), user.getName());
    }
}