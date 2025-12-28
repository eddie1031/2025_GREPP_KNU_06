package io.eddie.demo.domain.payments.repository;

import io.eddie.demo.domain.payments.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderCode(String orderCode);

}
