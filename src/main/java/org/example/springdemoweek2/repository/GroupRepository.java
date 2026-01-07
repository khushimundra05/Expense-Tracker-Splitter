package org.example.springdemoweek2.repository;

import org.example.springdemoweek2.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
