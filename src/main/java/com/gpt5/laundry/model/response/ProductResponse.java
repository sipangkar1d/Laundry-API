package com.gpt5.laundry.model.response;

import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class ProductResponse {
    private String id;
    private String name;
    private Integer stock;
    private Long price;
}
