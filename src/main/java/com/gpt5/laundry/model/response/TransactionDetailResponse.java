package com.gpt5.laundry.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
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
