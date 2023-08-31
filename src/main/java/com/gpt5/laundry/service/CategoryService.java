package com.gpt5.laundry.service;

import com.gpt5.laundry.entity.Category;
import com.gpt5.laundry.model.request.CategoryRequest;
import com.gpt5.laundry.model.response.CategoryResponse;
import org.springframework.data.domain.Page;

public interface CategoryService {
    CategoryResponse create(CategoryRequest request);

    Category getById(String id);

    CategoryResponse getByIdResponse(String id);

    Page<CategoryResponse> getAll(String keyword, String sortBy, String direction, Integer page, Integer size);

    CategoryResponse update(CategoryRequest request);

    void deleteById(String id);
}
