package io.eddie.demo.domain.products.mapper;

import io.eddie.demo.domain.products.model.dto.ProductDescription;
import io.eddie.demo.domain.products.model.entity.Product;

public class ProductMapper {

    public static ProductDescription toDescription(Product product) {
        return new ProductDescription(
                product.getCode(),
                product.getAccountCode(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCreatedAt()
        );
    }

}
