package com.gpt5.laundry.service.impl;

import com.gpt5.laundry.entity.UserCredential;
import com.gpt5.laundry.entity.UserDetailsImpl;
import com.gpt5.laundry.repository.UserCredentialRepository;
import com.gpt5.laundry.service.UserCredentialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCredentialServiceImpl implements UserCredentialService {
    private final UserCredentialRepository userCredentialRepository;
    @Value(value = "${laundry.email}")
    private String defaultEmail;

    @Override
    public UserCredential getByEmail(String email) {
        log.info("start get userCredential by Email");

        UserCredential userCredential = userCredentialRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user credential not found"));

        log.info("end get userCredential by Email");
        return userCredential;
    }

    @Override
    public UserCredential getByToken(Authentication authentication) {
        log.info("start get userCredential by Token");

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UserCredential userCredential = getByEmail(userDetails.getEmail());

        log.info("end get userCredential by Token");
        return userCredential;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserCredential create(UserCredential request) {
        log.info("start create user credential");

        UserCredential userCredential = userCredentialRepository.saveAndFlush(request);

        log.info("end create user credential");
        return userCredential;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void activate(String email) {
        log.info("start activate user credential");

        UserCredential userCredential = getByEmail(email);
        userCredential.setIsActive(true);
        userCredentialRepository.save(userCredential);

        log.info("start activate user credential");
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deactivate(String email) {
        log.info("start deactivate user credential");

        if (email.equals(defaultEmail)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "default admin cannot deactivated");
        }

        UserCredential userCredential = getByEmail(email);
        userCredential.setIsActive(false);
        userCredentialRepository.save(userCredential);

        log.info("end deactivate user credential");
    }
}
