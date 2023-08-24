package com.gpt5.laundry.repository;

import com.gpt5.laundry.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
