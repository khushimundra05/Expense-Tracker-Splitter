package org.example.springdemoweek2.dto;

import java.math.BigDecimal;

public class ExpenseSummaryDto {

    private Long id;
    private String description;
    private BigDecimal amount;
    private String paidByUserName;

    public ExpenseSummaryDto(Long id, String description, BigDecimal amount, String paidByUserName) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.paidByUserName = paidByUserName;
    }

    public Long getId() { return id; }
    public String getDescription() { return description; }
    public BigDecimal getAmount() { return amount; }
    public String getPaidByUserName() { return paidByUserName; }
}
