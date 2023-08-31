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
public class CategoryRequest {
    private String id;
    @NotBlank(message = "name tidak boleh kosong")
    private String name;
    @NotBlank(message = "description tidak boleh kosong")
    private String description;
    @NotNull(message = "price tidak boleh kosong")
    private Long price;
}
