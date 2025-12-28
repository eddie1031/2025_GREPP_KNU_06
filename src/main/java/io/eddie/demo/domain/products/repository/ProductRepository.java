package io.eddie.demo.domain.products.repository;

import io.eddie.demo.domain.products.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
    select
        p
    from
        Product p
    where
        p.code = :code
    and
        p.deleteStatus = 'N'
    """)
    Optional<Product> findByCode(String code);

    @Query("""
    select
        p
    from
        Product p
    where
        p.code = :code
    and
        p.accountCode = :accountCode
    and
        p.deleteStatus = 'N'
    """)
    Optional<Product> findOwnerProductByCode(String code, String accountCode);

    @Query("""
    select
        p
    from
        Product p
    where
        p.deleteStatus = 'N'
    """)
    Page<Product> findProducts(Pageable pageable);

}
