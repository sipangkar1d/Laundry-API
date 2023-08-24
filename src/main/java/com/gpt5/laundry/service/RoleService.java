package com.gpt5.laundry.service;

import com.gpt5.laundry.entity.Role;
import com.gpt5.laundry.entity.constant.ERole;

public interface RoleService {
    Role getOrSave(ERole role);
}
