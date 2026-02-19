package org.example.springdemoweek2.service;

import org.example.springdemoweek2.dto.ExpenseSummaryDto;
import org.example.springdemoweek2.model.*;
import org.example.springdemoweek2.repository.*;
import org.example.springdemoweek2.exception.ResourceNotFoundException;
import org.example.springdemoweek2.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

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
            BigDecimal amount) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

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
        BigDecimal splitAmount =amount.divide(
                BigDecimal.valueOf(members.size()),
                2,
                RoundingMode.HALF_UP
        );

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

    public List<ExpenseSummaryDto> getExpensesForGroup(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        return expenseRepository.findByGroup(group).stream()
                .map(e -> new ExpenseSummaryDto(
                        e.getId(),
                        e.getDescription(),
                        e.getAmount(),
                        e.getPaidBy().getName()
                ))
                .collect(Collectors.toList());
    }
}
