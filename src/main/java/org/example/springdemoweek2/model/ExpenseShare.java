
package org.example.springdemoweek2.model;

import jakarta.persistence.*;

@Entity
@Table(name = "expense_shares")
public class ExpenseShare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "expense_id", nullable = false)
    private Expense expense;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Double amount;

    public ExpenseShare() {}

    public ExpenseShare(Expense expense, User user, Double amount) {
        this.expense = expense;
        this.user = user;
        this.amount = amount;
    }


}