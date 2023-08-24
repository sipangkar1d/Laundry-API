package com.gpt5.laundry.repository;

import com.gpt5.laundry.entity.Category;
import com.gpt5.laundry.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomerRepository extends JpaRepository<Customer, String>, JpaSpecificationExecutor<Customer> {
}
