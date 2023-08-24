package com.gpt5.laundry.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TransactionResponse {
    private String customerName;
    private String invoice;
    private Date date;
    private Boolean Status;
    private Long grandTotal;
    private List<TransactionDetailResponse> transactionDetailResponses;
}
