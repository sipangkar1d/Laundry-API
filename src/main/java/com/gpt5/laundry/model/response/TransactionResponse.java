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
    private String id;
    private String customerName;
    private String invoice;
    private String date;
    private Boolean isPaid;
    private String status;
    private Long grandTotal;
    private List<TransactionDetailResponse> transactionDetailResponses;
}
