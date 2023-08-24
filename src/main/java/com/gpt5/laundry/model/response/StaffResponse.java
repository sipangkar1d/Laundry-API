package com.gpt5.laundry.model.response;

import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class StaffResponse {
    private String id;
    private String name;
    private String phone;
    private String address;
    private String email;
}
