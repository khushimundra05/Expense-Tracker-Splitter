package org.example.springdemoweek2.controller;

import org.example.springdemoweek2.model.Group;
import org.example.springdemoweek2.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestParam String name) {
        Group group = groupService.createGroup(name);
        return ResponseEntity.status(201).body(group);
    }

    @PostMapping("/{groupId}/users/{userId}")
    public ResponseEntity<String> addUserToGroup(
            @PathVariable Long groupId,
            @PathVariable Long userId) {

        groupService.addUserToGroup(groupId, userId);
        return ResponseEntity.ok("User added to group");
    }
}

