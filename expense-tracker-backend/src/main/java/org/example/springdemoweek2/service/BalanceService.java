package org.example.springdemoweek2.service;

import org.example.springdemoweek2.dto.BalanceResponseDto;
import org.example.springdemoweek2.dto.SettlementDto;
import org.example.springdemoweek2.exception.ResourceNotFoundException;
import org.example.springdemoweek2.model.Expense;
import org.example.springdemoweek2.model.ExpenseShare;
import org.example.springdemoweek2.model.Group;
import org.example.springdemoweek2.model.User;
import org.example.springdemoweek2.repository.ExpenseRepository;
import org.example.springdemoweek2.repository.ExpenseShareRepository;
import org.example.springdemoweek2.repository.GroupRepository;
import org.example.springdemoweek2.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class BalanceService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseShareRepository expenseShareRepository;
    private final GroupRepository groupRepository;
    private final GroupService groupService;
    private final UserRepository userRepository;

    public BalanceService(
            ExpenseRepository expenseRepository,
            ExpenseShareRepository expenseShareRepository,
            GroupRepository groupRepository,
            GroupService groupService,
            UserRepository userRepository) {

        this.expenseRepository = expenseRepository;
        this.expenseShareRepository = expenseShareRepository;
        this.groupRepository = groupRepository;
        this.groupService = groupService;
        this.userRepository = userRepository;
    }

    /**
     * Balance = total paid by user - total owed (shares) in this group.
     * Positive = others owe this user; negative = this user owes others.
     * Keys by userId (Long) so multiple expenses/shares for the same user aggregate correctly.
     */
    public List<BalanceResponseDto> calculateBalances(Long groupId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        List<User> members = groupService.getUsersOfGroup(groupId);
        Map<Long, BigDecimal> balanceByUserId = new HashMap<>();

        for (User u : members) {
            balanceByUserId.put(u.getId(), BigDecimal.ZERO);
        }

        // Add: what each user paid
        List<Expense> expenses = expenseRepository.findByGroup(group);
        for (Expense expense : expenses) {
            Long paidById = expense.getPaidBy().getId();
            // Ensure payer is in map (they might not be in current members if they left the group)
            balanceByUserId.putIfAbsent(paidById, BigDecimal.ZERO);
            balanceByUserId.put(
                    paidById,
                    balanceByUserId.get(paidById).add(expense.getAmount())
            );
        }

        // Subtract: what each user owes (their share of each expense)
        List<ExpenseShare> shares = expenseShareRepository.findByExpense_Group(group);
        for (ExpenseShare share : shares) {
            Long userId = share.getUser().getId();
            // Ensure user is in map (they might have shares from when they were in the group)
            balanceByUserId.putIfAbsent(userId, BigDecimal.ZERO);
            balanceByUserId.put(
                    userId,
                    balanceByUserId.get(userId).subtract(share.getAmount())
            );
        }

        List<BalanceResponseDto> response = new ArrayList<>();
        for (Map.Entry<Long, BigDecimal> e : balanceByUserId.entrySet()) {
            User user = userRepository.findById(e.getKey()).orElse(null);
            String name = user != null ? user.getName() : "Unknown";
            response.add(new BalanceResponseDto(e.getKey(), name, e.getValue()));
        }
        return response;
    }

    /**
     * Pairwise "who owes whom": for each pair, net debt (from owes to).
     * E.g. B owes A ₹66.67, C owes A ₹166.67, C owes B ₹100.
     * Uses expense shares: each share is "this user owes the payer this amount"; net per pair.
     */
    public List<SettlementDto> calculatePairwiseDebts(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        // debt[fromUserId][toUserId] = total amount that from owes to
        Map<Long, Map<Long, BigDecimal>> debtFromTo = new HashMap<>();

        List<ExpenseShare> shares = expenseShareRepository.findByExpense_Group(group);
        for (ExpenseShare share : shares) {
            Long debtorId = share.getUser().getId();
            Long creditorId = share.getExpense().getPaidBy().getId();
            if (debtorId.equals(creditorId)) continue;

            debtFromTo.putIfAbsent(debtorId, new HashMap<>());
            Map<Long, BigDecimal> row = debtFromTo.get(debtorId);
            row.put(creditorId, row.getOrDefault(creditorId, BigDecimal.ZERO).add(share.getAmount()));
        }

        // Net pairwise: for each (i,j) with i < j, net = debt(i,j) - debt(j,i). If net > 0, i owes j. If net < 0, j owes i.
        BigDecimal epsilon = new BigDecimal("0.01");
        List<SettlementDto> result = new ArrayList<>();
        Set<Long> allIds = new HashSet<>();
        for (Map.Entry<Long, Map<Long, BigDecimal>> e : debtFromTo.entrySet()) {
            allIds.add(e.getKey());
            allIds.addAll(e.getValue().keySet());
        }

        List<Long> sortedIds = new ArrayList<>(allIds);
        Collections.sort(sortedIds);
        for (int i = 0; i < sortedIds.size(); i++) {
            for (int j = i + 1; j < sortedIds.size(); j++) {
                Long idI = sortedIds.get(i);
                Long idJ = sortedIds.get(j);
                BigDecimal debtIJ = debtFromTo.getOrDefault(idI, Collections.emptyMap()).getOrDefault(idJ, BigDecimal.ZERO);
                BigDecimal debtJI = debtFromTo.getOrDefault(idJ, Collections.emptyMap()).getOrDefault(idI, BigDecimal.ZERO);
                BigDecimal net = debtIJ.subtract(debtJI).setScale(2, RoundingMode.HALF_UP);
                if (net.compareTo(epsilon) > 0) {
                    User from = userRepository.findById(idI).orElse(null);
                    User to = userRepository.findById(idJ).orElse(null);
                    result.add(new SettlementDto(
                            idI, from != null ? from.getName() : "?",
                            idJ, to != null ? to.getName() : "?",
                            net
                    ));
                } else if (net.compareTo(epsilon.negate()) < 0) {
                    User from = userRepository.findById(idJ).orElse(null);
                    User to = userRepository.findById(idI).orElse(null);
                    result.add(new SettlementDto(
                            idJ, from != null ? from.getName() : "?",
                            idI, to != null ? to.getName() : "?",
                            net.negate()
                    ));
                }
            }
        }
        return result;
    }
}
