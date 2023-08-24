package com.gpt5.laundry.service;

import com.gpt5.laundry.entity.Customer;

import java.util.List;

public interface CustomerService {
    Customer create(Customer customer);

    Customer update(Customer customer);

    Customer getById(String id);

    List<Customer> getAll();
}
