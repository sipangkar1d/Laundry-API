package com.gpt5.laundry.model.request;

import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class StaffRequest {
    private String name;
    private String phone;
    private String address;
    private String email;
}
