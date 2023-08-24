package com.gpt5.laundry.repository;

import com.gpt5.laundry.entity.Category;
import com.gpt5.laundry.entity.CategoryPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryPriceRepository extends JpaRepository<CategoryPrice, String> {
}
