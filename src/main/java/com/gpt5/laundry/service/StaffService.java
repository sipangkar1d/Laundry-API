package com.gpt5.laundry.service;

import com.gpt5.laundry.entity.Product;
import com.gpt5.laundry.entity.Staff;

import java.util.List;

public interface StaffService {
    Staff create(Staff product);

    Staff getById(String id);

    List<Staff> getAll();

    Staff update(Staff product);

    void deleteById(String id);
}
