package com.gpt5.laundry.repository;

import com.gpt5.laundry.entity.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, String> {
    List<ProductPrice> findAllByIsActiveTrueAndProduct_Id(String id);
}
