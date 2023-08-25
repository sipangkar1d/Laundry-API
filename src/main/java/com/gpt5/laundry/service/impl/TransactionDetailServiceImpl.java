package com.gpt5.laundry.service.impl;

import com.gpt5.laundry.entity.TransactionDetail;
import com.gpt5.laundry.model.response.TransactionDetailResponse;
import com.gpt5.laundry.repository.TransactionDetailRepository;
import com.gpt5.laundry.service.TransactionDetailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class TransactionDetailServiceImpl implements TransactionDetailService {
    private final TransactionDetailRepository transactionDetailRepository;

    @Override
    public TransactionDetail create(TransactionDetail request) {
        log.info("start crete detail transaction");

        TransactionDetail transactionDetail = transactionDetailRepository.saveAndFlush(request);

        log.info("end crete detail transaction");
        return transactionDetail;
    }
}
