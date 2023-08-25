package com.gpt5.laundry.service;

import com.gpt5.laundry.entity.UserCredential;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface UserCredentialService {
    UserCredential getByEmail(String email);

    UserCredential getByToken(Authentication authentication);

    UserCredential create(UserCredential userCredential);

    void activate(String email);

    void deactivate(String email);

    Optional<UserCredential> getDefaultUser(String email);
}
