package org.example.springdemoweek2.controller;

import org.example.springdemoweek2.dto.CreateExpenseRequest;
import org.example.springdemoweek2.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<String> addExpense(
            @Valid @RequestBody CreateExpenseRequest request) {

        expenseService.addExpense(
                request.getGroupId(),
                request.getPaidByUserId(),
                request.getDescription(),
                request.getAmount()
        );

        return ResponseEntity.status(201)
                .body("Expense added successfully");
    }
}
