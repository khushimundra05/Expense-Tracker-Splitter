package org.example.springdemoweek2.service;

import org.example.springdemoweek2.dto.BalanceResponseDto;
import org.example.springdemoweek2.dto.UserGroupBalanceDto;
import org.example.springdemoweek2.dto.UserSummaryResponseDto;
import org.example.springdemoweek2.exception.UserNotFoundException;
import org.example.springdemoweek2.model.Group;
import org.example.springdemoweek2.model.GroupMember;
import org.example.springdemoweek2.repository.GroupMemberRepository;
import org.example.springdemoweek2.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserSummaryService {

    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final BalanceService balanceService;

    public UserSummaryService(UserRepository userRepository,
                              GroupMemberRepository groupMemberRepository,
                              BalanceService balanceService) {
        this.userRepository = userRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.balanceService = balanceService;
    }

    public UserSummaryResponseDto getUserSummary(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<GroupMember> memberships = groupMemberRepository.findByUser_Id(userId);
        List<UserGroupBalanceDto> groupBalances = new ArrayList<>();
        BigDecimal totalNet = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        for (GroupMember m : memberships) {
            Group group = m.getGroup();
            List<BalanceResponseDto> balances = balanceService.calculateBalances(group.getId());
            BigDecimal userBalance = balances.stream()
                    .filter(b -> b.getUserId().equals(userId))
                    .map(BalanceResponseDto::getBalance)
                    .findFirst()
                    .orElse(BigDecimal.ZERO);

            userBalance = userBalance.setScale(2, RoundingMode.HALF_UP);
            groupBalances.add(new UserGroupBalanceDto(group.getId(), group.getName(), userBalance));
            totalNet = totalNet.add(userBalance);
        }

        totalNet = totalNet.setScale(2, RoundingMode.HALF_UP);
        return new UserSummaryResponseDto(user.getId(), user.getName(), groupBalances, totalNet);
    }
}
