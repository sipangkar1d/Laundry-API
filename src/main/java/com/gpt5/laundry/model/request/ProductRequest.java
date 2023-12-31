package com.gpt5.laundry.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private String id;

    @NotBlank(message = "name tidak boleh kosong")
    private String name;

    @NotNull(message = "stock tidak boleh kosong")
    private Integer stock;

    @NotNull(message = "price tidak boleh kosong")
    private Long price;
}
