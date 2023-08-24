package com.gpt5.laundry.repository;

import com.gpt5.laundry.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository extends JpaRepository<Product, String> , JpaSpecificationExecutor<Product> {
}
