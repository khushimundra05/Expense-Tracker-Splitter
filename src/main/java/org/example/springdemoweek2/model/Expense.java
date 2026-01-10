package org.example.springdemoweek2.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    @JsonIgnore
    private Group group;

    @ManyToOne
    @JoinColumn(name = "paid_by", nullable = false)
    private User paidBy;

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL)
    private List<ExpenseShare> shares;

    public Expense() {}

    public Expense(String description, BigDecimal amount, Group group, User paidBy) {
        this.description = description;
        this.amount = amount;
        this.group = group;
        this.paidBy = paidBy;
    }


    public User getPaidBy() {
        return paidBy;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
