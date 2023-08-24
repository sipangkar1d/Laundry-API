package com.gpt5.laundry.repository;

import com.gpt5.laundry.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, String> {
}
