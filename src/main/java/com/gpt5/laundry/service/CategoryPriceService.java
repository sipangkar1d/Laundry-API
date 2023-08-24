package com.gpt5.laundry.service;

import com.gpt5.laundry.entity.CategoryPrice;

public interface CategoryPriceService {
    CategoryPrice create(CategoryPrice categoryPrice);
    CategoryPrice getById(String id);
    CategoryPrice update(CategoryPrice categoryPrice);
}
