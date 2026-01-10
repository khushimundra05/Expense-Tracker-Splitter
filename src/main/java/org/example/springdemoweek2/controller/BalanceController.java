package org.example.springdemoweek2.controller;

import org.example.springdemoweek2.dto.BalanceResponseDto;
import org.example.springdemoweek2.service.BalanceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/balances")
public class BalanceController {

    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping("/{groupId}")
    public List<BalanceResponseDto> getBalances(@PathVariable Long groupId) {
        return balanceService.calculateBalances(groupId);
    }
}
