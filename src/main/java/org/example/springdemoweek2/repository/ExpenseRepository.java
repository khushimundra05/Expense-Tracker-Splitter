package org.example.springdemoweek2.repository;

import org.example.springdemoweek2.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
