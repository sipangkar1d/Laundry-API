package com.gpt5.laundry.service;

import com.gpt5.laundry.entity.TransactionDetail;
import com.gpt5.laundry.model.request.TransactionDetailRequest;
import com.gpt5.laundry.model.response.TransactionDetailResponse;

public interface TransactionDetailService {
    TransactionDetail create(TransactionDetailRequest request);
}
