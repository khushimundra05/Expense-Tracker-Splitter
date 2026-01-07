package org.example.springdemoweek2.model;

import jakarta.persistence.*;//allows to import JPA annotations
import java.util.List;

@Entity //class should be mapped to a DB table
@Table(name="groups")
//public class because Hibernate and Spring need to access it
public class Group {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) //auto generate id - auto increment column
    private Long id;

    @Column(nullable = false)
    private String name;

    //One group has many members
    @OneToMany(mappedBy = "group",cascade = CascadeType.ALL)
    private List<GroupMember> members;

    //One group has many expenses
    @OneToMany(mappedBy = "group",cascade = CascadeType.ALL)
    private List<Expense> expenses;

    public Group(String name) {
        this.name = name;
    }

    public Group() {}

    // getters & setters
    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}


