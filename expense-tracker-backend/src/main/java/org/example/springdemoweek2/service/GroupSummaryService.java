package org.example.springdemoweek2.service;

import org.example.springdemoweek2.dto.*;
import org.example.springdemoweek2.model.Group;
import org.example.springdemoweek2.repository.GroupRepository;
import org.example.springdemoweek2.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupSummaryService {

    private final GroupRepository groupRepository;
    private final BalanceService balanceService;
    private final SettlementService settlementService;

    public GroupSummaryService(GroupRepository groupRepository,
                               BalanceService balanceService,
                               SettlementService settlementService) {
        this.groupRepository = groupRepository;
        this.balanceService = balanceService;
        this.settlementService = settlementService;
    }

    public GroupSummaryResponseDto getGroupSummary(Long groupId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        // Calculate balances (keep this list unchanged for response)
        List<BalanceResponseDto> balances =
                balanceService.calculateBalances(groupId);

        // Who owes whom (pairwise net): B owes A ₹66.67, C owes A ₹166.67, etc.
        List<SettlementDto> whoOwesWhom = balanceService.calculatePairwiseDebts(groupId);

        // Minimum-transaction settlements — use a copy so balances are not mutated
        List<BalanceResponseDto> balancesCopy = balances.stream()
                .map(b -> new BalanceResponseDto(b.getUserId(), b.getUserName(), b.getBalance()))
                .collect(Collectors.toList());
        List<SettlementDto> settlements = settlementService.calculateSettlements(balancesCopy);

        // Calculate total expense (sum of positive balances)
        BigDecimal totalExpense = balances.stream()
                .map(BalanceResponseDto::getBalance)
                .filter(b -> b.compareTo(BigDecimal.ZERO) > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new GroupSummaryResponseDto(
                group.getId(),
                group.getName(),
                totalExpense,
                balances,
                whoOwesWhom,
                settlements
        );
    }

}

