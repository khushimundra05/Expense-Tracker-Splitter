package org.example.springdemoweek2.repository;

import org.example.springdemoweek2.model.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    List<GroupMember> findByUser_Id(Long userId);
}
