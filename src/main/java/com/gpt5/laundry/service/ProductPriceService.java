package com.gpt5.laundry.service;

import com.gpt5.laundry.entity.ProductPrice;

import java.util.List;

public interface ProductPriceService {
    ProductPrice create(ProductPrice productPrice);

    ProductPrice getById(String id);

    void deactivate(ProductPrice productPrice);

    List<ProductPrice> getProductPriceIsActiveAndProduct_Id(String id);
}
