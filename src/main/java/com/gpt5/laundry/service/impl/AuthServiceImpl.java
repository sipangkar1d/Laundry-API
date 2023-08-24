package com.gpt5.laundry.service.impl;

import com.gpt5.laundry.entity.Role;
import com.gpt5.laundry.entity.UserCredential;
import com.gpt5.laundry.entity.UserDetailsImpl;
import com.gpt5.laundry.entity.constant.ERole;
import com.gpt5.laundry.model.request.LoginRequest;
import com.gpt5.laundry.model.request.RegisterRequest;
import com.gpt5.laundry.model.request.StaffRequest;
import com.gpt5.laundry.model.response.LoginResponse;
import com.gpt5.laundry.model.response.RegisterResponse;
import com.gpt5.laundry.security.BCryptUtils;
import com.gpt5.laundry.security.JwtUtils;
import com.gpt5.laundry.service.AuthService;
import com.gpt5.laundry.service.RoleService;
import com.gpt5.laundry.service.StaffService;
import com.gpt5.laundry.service.UserCredentialService;
import com.gpt5.laundry.util.ValidationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserCredentialService userCredentialService;
    private final RoleService roleService;
    private final StaffService staffService;
    private final AuthenticationManager authenticationManager;
    private final ValidationUtils validationUtils;
    private final BCryptUtils bCryptUtils;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public RegisterResponse register(RegisterRequest request) {
        log.info("start register staff");
        validationUtils.validate(request);

        RegisterResponse response;
        try {
            Role role = roleService.getOrSave(ERole.ROLE_STAFF);
            UserCredential userCredential = userCredentialService.create(
                    UserCredential.builder()
                            .email(request.getEmail())
                            .password(bCryptUtils.hashPassword(request.getPassword()))
                            .roles(List.of(role))
                            .isActive(true)
                            .build());

            staffService.create(
                    StaffRequest.builder()
                            .name(request.getName())
                            .email(userCredential.getEmail())
                            .address(request.getAddress())
                            .phone(request.getPhone())
                            .build());

            response = RegisterResponse.builder()
                    .roles(List.of(String.valueOf(role)))
                    .email(userCredential.getEmail())
                    .build();
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email sudah terdaftar");
        }

        log.info("end register staff");
        return response;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        UserDetailsImpl userDetail = (UserDetailsImpl) authenticate.getPrincipal();

        List<String> roles = userDetail.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String token = jwtUtils.generateToken(userDetail.getEmail());
        return LoginResponse.builder()
                .email(userDetail.getEmail())
                .roles(roles)
                .token(token)
                .build();
    }
}
