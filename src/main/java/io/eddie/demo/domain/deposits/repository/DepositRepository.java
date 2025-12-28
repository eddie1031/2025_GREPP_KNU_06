package io.eddie.demo.domain.deposits.repository;

import io.eddie.demo.domain.deposits.model.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DepositRepository extends JpaRepository<Deposit, Long> {

    Optional<Deposit> findByAccountCode(String accountCode);

    @Query("""
    select
        case
            when count(d) > 0 then true
            else false
        end
    from
        Deposit d
    where
        d.code = :code
    and
        d.balance >= :amount
    """)
    boolean hasEnoughBalance(String depositCode, Long amount);

}
