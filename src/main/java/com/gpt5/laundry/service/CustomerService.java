package com.gpt5.laundry.service;

import com.gpt5.laundry.entity.Customer;
import org.springframework.data.domain.Page;

public interface CustomerService {
    Customer create(Customer customer);

    Customer update(Customer customer);

    Customer getById(String id);

    Page<Customer> getAll(String keyword, Integer page, Integer size, String sortBy, String direction);
}
