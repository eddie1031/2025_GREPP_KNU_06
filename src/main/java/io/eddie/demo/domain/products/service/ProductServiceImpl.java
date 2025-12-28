package io.eddie.demo.domain.products.service;

import io.eddie.demo.domain.products.model.dto.UpsertProductRequest;
import io.eddie.demo.domain.products.model.entity.Product;
import io.eddie.demo.domain.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Product save(String accountCode, UpsertProductRequest request) {

        Product product = Product.builder()
                .accountCode(accountCode)
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .build();

        return productRepository.save(product);

    }

    @Override
    @Transactional(readOnly = true)
    public Product getByCode(String code) {
        return productRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품은 존재하지 않습니다."));
    }

    @Override
    @Transactional
    public Product update(String accountCode, String productCode, UpsertProductRequest request) {

        Product findProduct = productRepository.findOwnerProductByCode(productCode, accountCode)
                .orElseThrow(() -> new IllegalStateException("해당 상품은 존재하지 않습니다"));


        findProduct.update(request.name(), request.description(), request.price());

        return findProduct;

    }

    @Override
    @Transactional
    public String delete(String accountCode, String productCode) {

        Product targetProduct = productRepository.findOwnerProductByCode(productCode, accountCode)
                .orElseThrow(() -> new IllegalStateException("해당 상품은 존재하지 않습니다"));

        targetProduct.delete();

        return targetProduct.getCode();
    }

    @Override
    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findProducts(pageable);
    }


}
