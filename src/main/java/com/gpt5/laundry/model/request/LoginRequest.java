package com.gpt5.laundry.model.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class LoginRequest {
    @NotBlank(message = "email tidak boleh kosong")
    private String email;

    @NotBlank(message = "password tidak boleh kosong")
    private String password;
}
