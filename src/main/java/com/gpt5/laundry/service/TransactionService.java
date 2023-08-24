package com.gpt5.laundry.service;

import com.gpt5.laundry.entity.Transaction;
import com.gpt5.laundry.model.request.TransactionRequest;
import com.gpt5.laundry.model.response.TransactionResponse;

import java.util.List;

public interface TransactionService {
    TransactionResponse create(TransactionRequest request);

    TransactionResponse findById(String id);

    List<TransactionResponse> findAll();
}
