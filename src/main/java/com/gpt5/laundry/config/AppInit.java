package com.gpt5.laundry.config;

import com.gpt5.laundry.entity.UserCredential;
import com.gpt5.laundry.model.request.LoginRequest;
import com.gpt5.laundry.service.AuthService;
import com.gpt5.laundry.service.UserCredentialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class AppInit implements CommandLineRunner {
    private final UserCredentialService userCredentialService;
    private final AuthService authService;

    @Value(value = "${laundry.email}")
    String email;
    @Value(value = "${laundry.password}")
    String password;

    @Override
    public void run(String... args) {
        log.info("Checking first Admin was created.");
        Optional<UserCredential> userDefault = userCredentialService.getDefaultUser(email);
        if (userDefault.isEmpty()) {
            log.info("First Admin not found.");
            authService.registerAdmin(LoginRequest.builder()
                    .email(email)
                    .password(password)
                    .build());
        }
        log.info("First Admin was created.");
    }
}
