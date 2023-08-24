package com.gpt5.laundry.model.request;

import java.util.List;

public class TransactionRequest {
    private String customerPhone;
    private List<TransactionDetailRequest> orderDetails;
}
