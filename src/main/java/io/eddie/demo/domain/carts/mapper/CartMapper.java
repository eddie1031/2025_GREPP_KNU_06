package io.eddie.demo.domain.carts.mapper;

import io.eddie.demo.domain.carts.model.dto.CartDescription;
import io.eddie.demo.domain.carts.model.dto.CartItemDescription;
import io.eddie.demo.domain.carts.model.entity.Cart;
import io.eddie.demo.domain.carts.model.entity.CartItem;

public class CartMapper {

    public static CartDescription toCartDescription(Cart cart) {
        return new CartDescription(
                cart.getCode(),
                cart.getCartItems()
                        .stream()
                        .map(CartMapper::toCartItemDescription)
                        .toList(),
                cart.getTotalPrice()
        );
    }

    public static CartItemDescription toCartItemDescription(CartItem cartItem) {
        return new CartItemDescription(
                cartItem.getCode(),
                cartItem.getProductCode(),
                cartItem.getProductName(),
                cartItem.getProductPrice(),
                cartItem.getQuantity(),
                cartItem.getCreatedAt()
        );
    }

}
