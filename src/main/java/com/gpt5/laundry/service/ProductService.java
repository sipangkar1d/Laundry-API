package com.gpt5.laundry.service;

import com.gpt5.laundry.entity.Product;
import com.gpt5.laundry.model.request.ProductRequest;
import com.gpt5.laundry.model.response.ProductResponse;
import org.springframework.data.domain.Page;

public interface ProductService {
    ProductResponse create(ProductRequest request);

    Product getById(String id);

    Page<ProductResponse> getAll(String keyword, Integer page, Integer size, String sortBy, String direction);

    ProductResponse update(ProductRequest request);

    void deleteById(String id);
}
