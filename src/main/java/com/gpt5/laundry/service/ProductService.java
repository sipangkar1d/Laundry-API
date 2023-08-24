package com.gpt5.laundry.service;

import com.gpt5.laundry.entity.Category;
import com.gpt5.laundry.entity.Product;

import java.util.List;

public interface ProductService {
    Product create(Product product);

    Product getById(String id);

    List<Product> getAll();

    Product update(Product product);

    void deleteById(String id);
}
