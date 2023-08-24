package com.gpt5.laundry.repository;

import com.gpt5.laundry.entity.Category;
import com.gpt5.laundry.entity.TransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, String> {
}
