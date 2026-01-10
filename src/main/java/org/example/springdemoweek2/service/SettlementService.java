package org.example.springdemoweek2.service;

import org.example.springdemoweek2.dto.BalanceResponseDto;
import org.example.springdemoweek2.dto.SettlementDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class SettlementService {

    private static final BigDecimal ZERO = new BigDecimal("0.00");
    private static final BigDecimal EPSILON = new BigDecimal("0.01");

    public List<SettlementDto> calculateSettlements(List<BalanceResponseDto> balances) {

        Queue<BalanceResponseDto> debtors = new LinkedList<>();
        Queue<BalanceResponseDto> creditors = new LinkedList<>();

        for (BalanceResponseDto b : balances) {
            BigDecimal bal = normalize(b.getBalance());

            if (bal.compareTo(ZERO) < 0) {
                b.setBalance(bal);
                debtors.add(b);
            } else if (bal.compareTo(ZERO) > 0) {
                b.setBalance(bal);
                creditors.add(b);
            }
        }

        List<SettlementDto> settlements = new ArrayList<>();
        int safetyCounter = 0;

        while (!debtors.isEmpty() && !creditors.isEmpty()) {

            // 🚨 absolute safety guard
            if (++safetyCounter > 1000) {
                throw new IllegalStateException("Settlement loop exceeded safe limit");
            }

            BalanceResponseDto debtor = debtors.poll();
            BalanceResponseDto creditor = creditors.poll();

            BigDecimal settleAmount =
                    debtor.getBalance().abs().min(creditor.getBalance());

            settleAmount = normalize(settleAmount);

            if (settleAmount.compareTo(EPSILON) <= 0) {
                continue; // skip meaningless transactions
            }

            settlements.add(new SettlementDto(
                    debtor.getUserId(),
                    debtor.getUserName(),
                    creditor.getUserId(),
                    creditor.getUserName(),
                    settleAmount
            ));

            debtor.setBalance(normalize(debtor.getBalance().add(settleAmount)));
            creditor.setBalance(normalize(creditor.getBalance().subtract(settleAmount)));

            // ONLY requeue if balance is meaningful
            if (debtor.getBalance().abs().compareTo(EPSILON) > 0
                    && debtor.getBalance().compareTo(ZERO) < 0) {
                debtors.add(debtor);
            }

            if (creditor.getBalance().compareTo(EPSILON) > 0) {
                creditors.add(creditor);
            }
        }

        return settlements;
    }

    private BigDecimal normalize(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
