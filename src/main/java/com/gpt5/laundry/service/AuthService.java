package com.gpt5.laundry.service;

import com.gpt5.laundry.model.request.LoginRequest;
import com.gpt5.laundry.model.request.RegisterRequest;
import com.gpt5.laundry.model.response.LoginResponse;
import com.gpt5.laundry.model.response.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
    RegisterResponse registerAdmin(LoginRequest request);

    LoginResponse login(LoginRequest request);

}
