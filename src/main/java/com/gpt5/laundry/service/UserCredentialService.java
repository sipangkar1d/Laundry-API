package com.gpt5.laundry.service;

import com.gpt5.laundry.entity.UserCredential;

public interface UserCredentialService {
    UserCredential getByEmail(String email);
    UserCredential create(UserCredential userCredential);
    UserCredential activate(String email);
    UserCredential deactivate(String email);
}
