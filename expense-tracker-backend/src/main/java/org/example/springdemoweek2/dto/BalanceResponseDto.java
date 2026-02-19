package org.example.springdemoweek2.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BalanceResponseDto {

    private Long userId;
    private String userName;
    private BigDecimal balance;

    public BalanceResponseDto(Long userId, String userName, BigDecimal balance) {
        this.userId = userId;
        this.userName = userName;
        // Round here
        this.balance = balance.setScale(2, RoundingMode.HALF_UP);
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance != null ? balance.setScale(2, RoundingMode.HALF_UP) : null;
    }
}
