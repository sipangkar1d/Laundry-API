package com.gpt5.laundry.service;

import com.gpt5.laundry.entity.Customer;
import com.gpt5.laundry.model.request.CustomerRequest;
import org.springframework.data.domain.Page;

public interface CustomerService {
    Customer create(CustomerRequest customer);

    Customer update(CustomerRequest customer);

    Customer getById(String id);

    Page<Customer> getAll(String keyword, Integer page, Integer size, String sortBy, String direction);

    Customer getByPhone(String phone);
}
