package org.example.springdemoweek2.service;

import org.example.springdemoweek2.dto.*;
import org.example.springdemoweek2.model.Group;
import org.example.springdemoweek2.repository.GroupRepository;
import org.example.springdemoweek2.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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

        // Calculate balances
        List<BalanceResponseDto> balances =
                balanceService.calculateBalances(groupId);

        // TEMPORARILY disable settlement logic
        List<SettlementDto> settlements = List.of();

        // Calculate total expense (sum of positive balances)
        BigDecimal totalExpense = balances.stream()
                .map(BalanceResponseDto::getBalance)
                .filter(b -> b.compareTo(BigDecimal.ZERO) > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Return summary
        return new GroupSummaryResponseDto(
                group.getId(),
                group.getName(),
                totalExpense,
                balances,
                settlements
        );
    }

}

