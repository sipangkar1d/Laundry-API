package com.gpt5.laundry.service;

import com.gpt5.laundry.entity.CategoryPrice;

import java.util.List;

public interface CategoryPriceService {
    CategoryPrice create(CategoryPrice categoryPrice);
    CategoryPrice getById(String id);

    List<CategoryPrice> getAllIsActiveByCategory_Id(String id);
    void deactivate(CategoryPrice categoryPrice);
}
