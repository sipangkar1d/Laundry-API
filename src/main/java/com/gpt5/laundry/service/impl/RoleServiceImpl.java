package com.gpt5.laundry.service.impl;

import com.gpt5.laundry.entity.Role;
import com.gpt5.laundry.entity.constant.ERole;
import com.gpt5.laundry.repository.RoleRepository;
import com.gpt5.laundry.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;


    @Override
    public Role getOrSave(ERole role) {
        return roleRepository.findByRole(role)
                .orElseGet(() -> roleRepository.save(Role.builder().role(role).build()));
    }
}
