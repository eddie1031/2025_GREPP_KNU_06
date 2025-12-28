package io.eddie.demo.domain.settlements.repository;

import io.eddie.demo.domain.settlements.model.entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {

    Optional<Settlement> findByCode(String code);

}
