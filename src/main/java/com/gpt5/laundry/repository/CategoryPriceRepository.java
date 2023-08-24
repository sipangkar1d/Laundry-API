package com.gpt5.laundry.repository;

import com.gpt5.laundry.entity.CategoryPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryPriceRepository extends JpaRepository<CategoryPrice, String> {

    List<CategoryPrice> findAllByIsActiveTrueAndCategory_Id(String category_id);

}
