package org.example.springdemoweek2.repository;

import org.example.springdemoweek2.model.ExpenseShare;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseShareRepository extends JpaRepository<ExpenseShare, Long> {
}
