package com.gpt5.laundry.service.impl;

import com.gpt5.laundry.entity.Admin;
import com.gpt5.laundry.repository.AdminRepository;
import com.gpt5.laundry.service.AdminService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;

    @Override
    public Admin create(Admin admin) {
        return adminRepository.saveAndFlush(admin);
    }

    @Override
    public Admin getById(String id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "admin not found"));
    }

    @Override
    public Admin update(Admin admin) {
        return adminRepository.save(admin);
    }
}
