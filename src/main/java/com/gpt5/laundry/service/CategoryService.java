package com.gpt5.laundry.service;

import com.gpt5.laundry.entity.Category;

import java.util.List;

public interface CategoryService {
    Category create(Category category);

    Category getById(String id);

    List<Category> getAll();

    Category update(Category category);

    void deleteById(String id);
}
