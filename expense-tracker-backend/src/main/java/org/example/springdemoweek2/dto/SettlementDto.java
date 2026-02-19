package org.example.springdemoweek2.dto;

import java.math.BigDecimal;

public class SettlementDto {

    private Long fromUserId;
    private String fromUserName;

    private Long toUserId;
    private String toUserName;

    private BigDecimal amount;

    public SettlementDto(Long fromUserId, String fromUserName,
                         Long toUserId, String toUserName,
                         BigDecimal amount) {
        this.fromUserId = fromUserId;
        this.fromUserName = fromUserName;
        this.toUserId = toUserId;
        this.toUserName = toUserName;
        this.amount = amount;
    }

    public Long getFromUserId() { return fromUserId; }
    public String getFromUserName() { return fromUserName; }
    public Long getToUserId() { return toUserId; }
    public String getToUserName() { return toUserName; }
    public BigDecimal getAmount() { return amount; }
}
