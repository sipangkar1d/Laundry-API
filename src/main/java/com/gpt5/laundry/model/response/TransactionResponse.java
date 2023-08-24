package com.gpt5.laundry.model.response;

import lombok.Builder;
import lombok.Getter;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.Date;
import java.util.List;

@Getter
@Builder(toBuilder = true)
public class TransactionResponse {
    private String customerName;
    private String invoice;
    private Date date;
    private Boolean Status;
    private Long grandTotal;
    private List<TransactionDetailResponse> transactionDetailResponses;
}
