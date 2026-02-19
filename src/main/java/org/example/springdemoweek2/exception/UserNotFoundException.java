package org.example.springdemoweek2.exception;

//exceptions will be thrown in service layer
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message){
        super(message);
    }
}
