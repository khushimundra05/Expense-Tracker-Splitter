package org.example.springdemoweek2.dto;

import java.math.BigDecimal;
import java.util.List;

public class GroupSummaryResponseDto {

    private Long groupId;
    private String groupName;
    private BigDecimal totalExpense;
    private List<BalanceResponseDto> balances;
    /** Who owes whom (pairwise net): e.g. B owes A ₹66.67, C owes A ₹166.67 */
    private List<SettlementDto> whoOwesWhom;
    /** Minimal transactions to settle all debts: e.g. C pays A ₹233.33, C pays B ₹33.33 */
    private List<SettlementDto> settlements;

    public GroupSummaryResponseDto(
            Long groupId,
            String groupName,
            BigDecimal totalExpense,
            List<BalanceResponseDto> balances,
            List<SettlementDto> whoOwesWhom,
            List<SettlementDto> settlements
    ) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.totalExpense = totalExpense;
        this.balances = balances;
        this.whoOwesWhom = whoOwesWhom;
        this.settlements = settlements;
    }

    public Long getGroupId() { return groupId; }
    public String getGroupName() { return groupName; }
    public BigDecimal getTotalExpense() { return totalExpense; }
    public List<BalanceResponseDto> getBalances() { return balances; }
    public List<SettlementDto> getWhoOwesWhom() { return whoOwesWhom; }
    public List<SettlementDto> getSettlements() { return settlements; }
}
