package org.example.springdemoweek2.dto;

import java.math.BigDecimal;

public class UserGroupBalanceDto {

    private Long groupId;
    private String groupName;
    private BigDecimal balance;

    public UserGroupBalanceDto(Long groupId, String groupName, BigDecimal balance) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.balance = balance;
    }

    public Long getGroupId() { return groupId; }
    public String getGroupName() { return groupName; }
    public BigDecimal getBalance() { return balance; }
}
