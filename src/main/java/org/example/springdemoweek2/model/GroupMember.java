package org.example.springdemoweek2.model;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name="groupmember")
public class GroupMember {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    //many group members in one group
    @ManyToOne
    @JoinColumn(name="group_id",nullable=false) //foreign key that references to Group table
    private Group group; //This GroupMember object holds a reference to a Group object. For easy conversion of java object to SQL query

    //one user can be many groupmember (member of many groups)
    @ManyToOne
    @JoinColumn(name="user_id",nullable=false)
    private User user;

    public GroupMember() {}

    public GroupMember(Group group, User user) {
        this.group = group;
        this.user = user;
    }

    // getters
    public Long getId() { return id; }
    public Group getGroup() { return group; }
    public User getUser() { return user; }

}
