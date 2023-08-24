package com.gpt5.laundry.service;

import com.gpt5.laundry.entity.Admin;

import java.util.List;

public interface AdminService {
    Admin create(Admin admin);
    Admin getById(String id);
    Admin update(Admin admin);
}
