package com.gpt5.laundry.service.impl;

import com.gpt5.laundry.entity.TransactionDetail;
import com.gpt5.laundry.model.request.TransactionDetailRequest;
import com.gpt5.laundry.repository.TransactionDetailRepository;
import com.gpt5.laundry.service.TransactionDetailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class TransactionDetailServiceImpl implements TransactionDetailService {
    private final TransactionDetailRepository transactionDetailRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public TransactionDetail create(TransactionDetailRequest request) {
        log.info("start transaction");

        log.info("end transaction");
        return null;
    }
}
