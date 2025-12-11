package org.example.springdemoweek2.controller;
import org.example.springdemoweek2.dto.UserRequestDto;
import org.example.springdemoweek2.model.User;
import org.example.springdemoweek2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

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

    //initial version
//    @PostMapping("/user")
//    public User createUser(@RequestBody User user) {
//        return userService.createUser(user.getId(), user.getName());
//    }
    //@RequestBody converts JSON from request and converts it to the required (here - User) object
    //using response entity : allows to return proper HTTP body with status code & objects
    public ResponseEntity<User> createUser(@Valid @RequestBody UserRequestDto userDto) {
        User createdUser = userService.createUser(userDto.getId(), userDto.getName());
        return ResponseEntity.status(201).body(createdUser);
    }
}