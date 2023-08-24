package com.gpt5.laundry.service;

import com.gpt5.laundry.model.request.AuthRequest;
import com.gpt5.laundry.model.response.LoginResponse;
import com.gpt5.laundry.model.response.RegisterResponse;

public interface AuthService {
    RegisterResponse register(AuthRequest request);

    LoginResponse login(AuthRequest request);

}
