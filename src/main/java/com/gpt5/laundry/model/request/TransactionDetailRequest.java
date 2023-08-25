package com.gpt5.laundry.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
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
