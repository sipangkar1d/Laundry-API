package com.gpt5.laundry.model.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TransactionDetailRequest {
    @NotBlank(message = "category id is required")
    private String categoryId;
    @NotNull(message = "category quantity is required")
    private Integer quantity;
    @NotBlank(message = "product id is required")
    private String productId;
    @NotNull(message = "product quantity is required")
    private Integer productQuantity;
}
