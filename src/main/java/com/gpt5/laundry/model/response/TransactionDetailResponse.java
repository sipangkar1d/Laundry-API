package com.gpt5.laundry.model.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class TransactionDetailResponse {
    private String categoryName;
    private Integer quantity;
    private Long price;
    private String productName;
    private String productQuantity;
    private Long productPrice;
    private Long subTotal;

}
