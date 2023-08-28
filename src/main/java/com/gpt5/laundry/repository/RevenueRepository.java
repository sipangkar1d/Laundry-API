package com.gpt5.laundry.repository;

import com.gpt5.laundry.entity.Revenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface RevenueRepository extends JpaRepository<Revenue, String>, JpaSpecificationExecutor<Revenue> {
}
