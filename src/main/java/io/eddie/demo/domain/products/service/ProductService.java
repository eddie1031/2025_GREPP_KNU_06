package io.eddie.demo.domain.products.service;

import io.eddie.demo.domain.products.model.dto.UpsertProductRequest;
import io.eddie.demo.domain.products.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Product save(String accountCode, UpsertProductRequest request);
    Product getByCode(String code);


    Product update(String accountCode, String productCode, UpsertProductRequest request);

    String delete(String accountCode, String productCode);

    Page<Product> getProducts(Pageable pageable);

}
