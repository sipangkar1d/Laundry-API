package com.gpt5.laundry.repository;

import com.gpt5.laundry.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
