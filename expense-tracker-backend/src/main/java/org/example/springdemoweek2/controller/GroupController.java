package org.example.springdemoweek2.controller;

import org.example.springdemoweek2.dto.ExpenseSummaryDto;
import org.example.springdemoweek2.dto.GroupSummaryResponseDto;
import org.example.springdemoweek2.model.Group;
import org.example.springdemoweek2.model.User;
import org.example.springdemoweek2.service.ExpenseService;
import org.example.springdemoweek2.service.GroupService;
import org.example.springdemoweek2.service.GroupSummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupSummaryService groupSummaryService;
    private final GroupService groupService;
    private final ExpenseService expenseService;

    public GroupController(GroupService groupService,
                           GroupSummaryService groupSummaryService,
                           ExpenseService expenseService) {
        this.groupService = groupService;
        this.groupSummaryService = groupSummaryService;
        this.expenseService = expenseService;
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

    @GetMapping("/{groupId}/summary")
    public ResponseEntity<GroupSummaryResponseDto> getGroupSummary(
            @PathVariable Long groupId) {

        return ResponseEntity.ok(
                groupSummaryService.getGroupSummary(groupId)
        );
    }

    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups() {
        return ResponseEntity.ok(groupService.getAllGroups());
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<User>> getGroupMembers(@PathVariable Long groupId) {
        return ResponseEntity.ok(groupService.getUsersOfGroup(groupId));
    }

    @GetMapping("/{groupId}/expenses")
    public ResponseEntity<List<ExpenseSummaryDto>> getGroupExpenses(@PathVariable Long groupId) {
        return ResponseEntity.ok(expenseService.getExpensesForGroup(groupId));
    }

}
