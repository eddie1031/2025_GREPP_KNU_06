package io.eddie.demo.domain.carts.model.entity;

import io.eddie.demo.common.model.persistence.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Cart extends BaseEntity {

    private String accountCode;

    @OneToMany(mappedBy = "cart")
    List<CartItem> cartItems = new ArrayList<>();

    @Builder
    public Cart(String accountCode) {
        this.accountCode = accountCode;
    }

    public Long getTotalPrice() {
        return cartItems.stream()
                .map(CartItem::getItemPrice)
                .reduce(Long::sum)
                .orElse(0L);
    }

}
