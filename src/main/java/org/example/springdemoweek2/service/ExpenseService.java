package org.example.springdemoweek2.service;

import org.example.springdemoweek2.model.*;
import org.example.springdemoweek2.repository.*;
import org.example.springdemoweek2.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseShareRepository expenseShareRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;

    public ExpenseService(
            ExpenseRepository expenseRepository,
            ExpenseShareRepository expenseShareRepository,
            GroupRepository groupRepository,
            GroupMemberRepository groupMemberRepository,
            UserRepository userRepository) {

        this.expenseRepository = expenseRepository;
        this.expenseShareRepository = expenseShareRepository;
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.userRepository = userRepository;
    }

    //add new expense
    public void addExpense(
            Long groupId,
            Long paidByUserId,
            String description,
            Double amount) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User paidBy = userRepository.findById(paidByUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Expense expense = new Expense(description, amount, group, paidBy);
        expenseRepository.save(expense);

        List<GroupMember> members = groupMemberRepository.findAll()
                .stream()
                .filter(m -> m.getGroup().getId().equals(groupId))
                .toList();

        //calculate split amount
        int totalMembers = members.size();
        Double splitAmount = amount / totalMembers;

        //add the split amount (expense share) for each member in group
        for (GroupMember member : members) {
            ExpenseShare share = new ExpenseShare(
                    expense,
                    member.getUser(),
                    splitAmount
            );
            expenseShareRepository.save(share);
        }
    }
}
