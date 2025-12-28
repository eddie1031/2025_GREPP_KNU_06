package io.eddie.demo.domain.carts.service;

import io.eddie.demo.domain.carts.model.entity.Cart;
import io.eddie.demo.domain.carts.model.entity.CartItem;
import io.eddie.demo.domain.carts.model.vo.CreateCartItemRequest;
import io.eddie.demo.domain.carts.repository.CartItemRepository;
import io.eddie.demo.domain.carts.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public Cart save(String accountCode) {

        Cart cart = Cart.builder()
                .accountCode(accountCode)
                .build();

        return cartRepository.save(cart);

    }

    @Override
    @Transactional(readOnly = true)
    public Cart getByAccountCode(String accountCode) {
        return cartRepository.findByAccountCode(accountCode)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 회원 코드가 입력되었습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public CartItem getItem(String accountCode, String cartItemCode) {
        return cartItemRepository.findOwnCartItem(accountCode, cartItemCode)
                .orElseThrow(() -> new IllegalArgumentException("해당 장바구니 항목을 찾을 수 없습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItem> getItemsByCodes(List<String> cartItemCodes) {
        return cartItemRepository.findAllByCodesIn(cartItemCodes);
    }

    @Override
    @Transactional
    public CartItem appendItem(String accountCode, CreateCartItemRequest request) {

        Cart targetCart = getByAccountCode(accountCode);

        CartItem cartItem = CartItem.builder()
                .cart(targetCart)
                .productCode(request.productCode())
                .productName(request.productName())
                .productPrice(request.productPrice())
                .quantity(request.quantity())
                .build();

        return cartItemRepository.save(cartItem);

    }

    @Override
    @Transactional
    public CartItem increaseQuantity(String accountCode, String cartItemCode) {

        CartItem targetItem = getItem(accountCode, cartItemCode);

        targetItem.increaseQuantity();

        return targetItem;
    }

    @Override
    @Transactional
    public CartItem decreaseQuantity(String accountCode, String cartItemCode) {

        CartItem targetItem = getItem(accountCode, cartItemCode);

        if ( targetItem.canDecrease() ) {
            targetItem.decreaseQuantity();
        }

        return targetItem;
    }

    @Override
    @Transactional
    public void deleteItem(String accountCode, String cartItemCode) {

        CartItem targetItem = getItem(accountCode, cartItemCode);
        cartItemRepository.delete(targetItem);

    }

    @Override
    @Transactional
    public void deleteItemsByCodes(List<String> cartItemCodes) {
        cartItemRepository.deleteAll(getItemsByCodes(cartItemCodes));
    }

}
