package org.example.springdemoweek2.repository;

import org.example.springdemoweek2.model.Expense;
import org.example.springdemoweek2.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByGroup(Group group);

}
