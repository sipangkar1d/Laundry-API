package com.gpt5.laundry.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    private String customerPhone;
    private Boolean isPaid;
    private List<TransactionDetailRequest> transactionDetailRequests;
}
