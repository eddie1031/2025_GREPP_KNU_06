package io.eddie.demo.domain.settlements.service;

import io.eddie.demo.domain.settlements.model.entity.Settlement;
import io.eddie.demo.domain.settlements.model.vo.SettlementStatus;

import java.util.List;

public interface SettlementService {

    List<Settlement> prepare(String dateStr);
    List<Settlement> process(String dateStr);

    boolean isProcessable(SettlementStatus status);

}
