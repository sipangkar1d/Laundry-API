package com.gpt5.laundry.service;

import com.gpt5.laundry.entity.UserCredential;
import org.springframework.security.core.Authentication;

public interface UserCredentialService {
    UserCredential getByEmail(String email);
    UserCredential getByToken(Authentication authentication);
    UserCredential create(UserCredential userCredential);
    void activate(String email);
    void deactivate(String email);
}
