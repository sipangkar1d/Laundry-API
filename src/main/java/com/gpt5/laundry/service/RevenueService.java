package com.gpt5.laundry.service;

import com.gpt5.laundry.entity.Revenue;

public interface RevenueService {
    Revenue create(Revenue request);

    Long getTotalRevenueThisMonth();
}
