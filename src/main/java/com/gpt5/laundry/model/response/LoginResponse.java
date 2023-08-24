package com.gpt5.laundry.model.response;

import lombok.*;

import java.util.List;

@Getter
@Builder(toBuilder = true)
public class LoginResponse {
    private String email;
    private List<String> roles;
    private String token;
}
