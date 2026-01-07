package org.example.springdemoweek2.service;

import org.example.springdemoweek2.model.Group;
import org.example.springdemoweek2.model.GroupMember;
import org.example.springdemoweek2.model.User;
import org.example.springdemoweek2.repository.GroupMemberRepository;
import org.example.springdemoweek2.repository.GroupRepository;
import org.example.springdemoweek2.repository.UserRepository;
import org.example.springdemoweek2.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;

    public GroupService(GroupRepository groupRepository,
                        UserRepository userRepository,
                        GroupMemberRepository groupMemberRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    //create group object and save to DB
    public Group createGroup(String name) {
        Group group = new Group(name);
        return groupRepository.save(group);
    }

    public void addUserToGroup(Long groupId, Long userId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        GroupMember member = new GroupMember(group, user);
        groupMemberRepository.save(member);
    }
}


