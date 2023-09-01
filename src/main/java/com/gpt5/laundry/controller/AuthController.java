package com.gpt5.laundry.controller;

import com.gpt5.laundry.model.request.LoginRequest;
import com.gpt5.laundry.model.request.RegisterRequest;
import com.gpt5.laundry.model.response.CommonResponse;
import com.gpt5.laundry.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        CommonResponse<?> loginSuccess = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .data(authService.login(request))
                .message("Login Success")
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(loginSuccess);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping(path = "/register")
    public ResponseEntity<?> registerStaff(@RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Success Register Admin")
                        .data(authService.register(request))
                        .build());
    }

    @GetMapping("/me")
    public ResponseEntity<?> getRoles(Authentication authentication) {
        return ResponseEntity.status(HttpStatus.OK).body(
                CommonResponse.builder()
                        .data(authService.getRole(authentication))
                        .build()
        );
    }
}
