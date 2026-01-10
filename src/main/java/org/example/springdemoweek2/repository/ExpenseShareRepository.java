package org.example.springdemoweek2.repository;

import org.example.springdemoweek2.model.ExpenseShare;
import org.example.springdemoweek2.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseShareRepository extends JpaRepository<ExpenseShare, Long> {
    List<ExpenseShare> findByExpenseGroup(Group group);
}
