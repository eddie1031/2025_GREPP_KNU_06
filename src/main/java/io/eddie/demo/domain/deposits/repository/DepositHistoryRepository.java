package io.eddie.demo.domain.deposits.repository;

import io.eddie.demo.domain.deposits.model.entity.DepositHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositHistoryRepository extends JpaRepository<DepositHistory, Long> {



}
