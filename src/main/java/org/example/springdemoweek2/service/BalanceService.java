package org.example.springdemoweek2.service;

import org.example.springdemoweek2.dto.BalanceResponseDto;
import org.example.springdemoweek2.model.Expense;
import org.example.springdemoweek2.model.ExpenseShare;
import org.example.springdemoweek2.model.Group;
import org.example.springdemoweek2.model.User;
import org.example.springdemoweek2.repository.ExpenseRepository;
import org.example.springdemoweek2.repository.ExpenseShareRepository;
import org.example.springdemoweek2.repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BalanceService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseShareRepository expenseShareRepository;
    private final GroupRepository groupRepository;
    private final GroupService groupService;

    public BalanceService(
            ExpenseRepository expenseRepository,
            ExpenseShareRepository expenseShareRepository,
            GroupRepository groupRepository,
            GroupService groupService) {

        this.expenseRepository = expenseRepository;
        this.expenseShareRepository = expenseShareRepository;
        this.groupRepository = groupRepository;
        this.groupService = groupService;
    }

    public List<BalanceResponseDto> calculateBalances(Long groupId) {

        // Validate group exists
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        Map<User, BigDecimal> balanceMap = new HashMap<>();

        // Initialize balances for group members
        List<User> users = groupService.getUsersOfGroup(groupId);
        for (User user : users) {
            balanceMap.put(user, BigDecimal.ZERO);
        }

        // Add amounts paid by users
        List<Expense> expenses = expenseRepository.findByGroup(group);
        for (Expense expense : expenses) {
            User paidBy = expense.getPaidBy();

            balanceMap.put(
                    paidBy,
                    balanceMap.getOrDefault(paidBy, BigDecimal.ZERO)
                            .add(expense.getAmount())
            );
        }

        //Subtract amounts owed by users
        List<ExpenseShare> shares = expenseShareRepository.findByExpenseGroup(group);
        for (ExpenseShare share : shares) {
            User user = share.getUser();

            balanceMap.put(
                    user,
                    balanceMap.getOrDefault(user, BigDecimal.ZERO)
                            .subtract(share.getAmount())
            );
        }

        // Convert to DTOs
        List<BalanceResponseDto> response = new ArrayList<>();
        for (Map.Entry<User, BigDecimal> entry : balanceMap.entrySet()) {
            response.add(
                    new BalanceResponseDto(
                            entry.getKey().getId(),
                            entry.getKey().getName(),
                            entry.getValue()
                    )
            );
        }

        return response;
    }
}
