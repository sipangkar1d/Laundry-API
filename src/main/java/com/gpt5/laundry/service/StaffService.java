package com.gpt5.laundry.service;

import com.gpt5.laundry.entity.Staff;
import com.gpt5.laundry.model.response.StaffResponse;
import org.springframework.data.domain.Page;

public interface StaffService {
    Staff create(Staff request);

    Staff getById(String id);

    Page<StaffResponse> getAll(String keyword, Integer page, Integer size, String sortBy, String direction);

    StaffResponse update(Staff staff);

    void deleteById(String id);
}
