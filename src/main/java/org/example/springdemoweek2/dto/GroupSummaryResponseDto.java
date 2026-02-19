package org.example.springdemoweek2.dto;

import java.math.BigDecimal;
import java.util.List;

public class GroupSummaryResponseDto {

    private Long groupId;
    private String groupName;
    private BigDecimal totalExpense;
    private List<BalanceResponseDto> balances;
    private List<SettlementDto> settlements;

    public GroupSummaryResponseDto(
            Long groupId,
            String groupName,
            BigDecimal totalExpense,
            List<BalanceResponseDto> balances,
            List<SettlementDto> settlements
    ) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.totalExpense = totalExpense;
        this.balances = balances;
        this.settlements = settlements;
    }

    public Long getGroupId() { return groupId; }
    public String getGroupName() { return groupName; }
    public BigDecimal getTotalExpense() { return totalExpense; }
    public List<BalanceResponseDto> getBalances() { return balances; }
    public List<SettlementDto> getSettlements() { return settlements; }
}
