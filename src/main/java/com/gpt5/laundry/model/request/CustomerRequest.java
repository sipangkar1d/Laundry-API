package com.gpt5.laundry.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {
    private String id;
    private String name;
    @NotBlank(message = "phone is required")
    private String phone;
    private String address;
}
