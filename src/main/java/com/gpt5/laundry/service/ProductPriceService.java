package com.gpt5.laundry.service;

import com.gpt5.laundry.entity.CategoryPrice;
import com.gpt5.laundry.entity.ProductPrice;

public interface ProductPriceService {
    ProductPrice create(ProductPrice productPrice);

    ProductPrice getById(String id);

    ProductPrice update(ProductPrice productPrice);
}
