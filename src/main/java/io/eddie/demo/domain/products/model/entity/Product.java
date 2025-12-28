package io.eddie.demo.domain.products.model.entity;

import io.eddie.demo.common.model.persistence.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    private String accountCode;

    private String name;
    private String description;
    private Long price;

    @Builder
    public Product(String accountCode, String name, String description, Long price) {
        this.accountCode = accountCode;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public void update(String name, String description, Long price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

}
