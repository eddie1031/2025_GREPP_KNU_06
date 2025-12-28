package io.eddie.demo.domain.carts.model.entity;

import io.eddie.demo.common.model.persistence.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class CartItem extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    // 상품 상세 캐싱
    private String productCode;

    private String productName;

    private Long productPrice;

    private Integer quantity;

    @Builder
    public CartItem(Cart cart, String productCode, String productName, Long productPrice, Integer quantity) {
        this.cart = cart;
        this.productCode = productCode;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public boolean canDecrease() {
        return this.quantity > 1;
    }

    public void increaseQuantity() {
        this.quantity++;
    }

    public void decreaseQuantity() {
        this.quantity--;
    }

    public Long getItemPrice() {
        return this.productPrice * this.quantity;
    }

}
