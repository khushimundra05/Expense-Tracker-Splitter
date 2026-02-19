package org.example.springdemoweek2.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity //This annotation makes this class a DB table
@Table(name = "Users")
public class User {
    @Id //primary key
    private Long id;
    private String name;

    public User() {} //this constructor is required to use Entity

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    //getters
    public Long getId() {return id;}
    public String getName() {return name;}

    //setters
    public void setId(Long id) {this.id = id;}
    public void setName(String name) {this.name = name;}
}

