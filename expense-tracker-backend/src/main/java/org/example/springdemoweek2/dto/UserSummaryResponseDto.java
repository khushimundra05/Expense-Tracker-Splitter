package org.example.springdemoweek2.dto;

import java.math.BigDecimal;
import java.util.List;

public class UserSummaryResponseDto {

    private Long userId;
    private String userName;
    private List<UserGroupBalanceDto> groupBalances;
    /** Total net: positive = others owe this user across all groups; negative = this user owes others. */
    private BigDecimal totalNetBalance;

    public UserSummaryResponseDto(Long userId, String userName,
                                  List<UserGroupBalanceDto> groupBalances,
                                  BigDecimal totalNetBalance) {
        this.userId = userId;
        this.userName = userName;
        this.groupBalances = groupBalances;
        this.totalNetBalance = totalNetBalance;
    }

    public Long getUserId() { return userId; }
    public String getUserName() { return userName; }
    public List<UserGroupBalanceDto> getGroupBalances() { return groupBalances; }
    public BigDecimal getTotalNetBalance() { return totalNetBalance; }
}
