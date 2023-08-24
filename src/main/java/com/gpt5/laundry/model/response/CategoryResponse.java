package com.gpt5.laundry.model.response;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder(toBuilder = true)
public class CategoryResponse {
    private String name;
    private String description;
    private Long price;
}
