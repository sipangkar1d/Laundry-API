package com.gpt5.laundry.repository;

import com.gpt5.laundry.entity.Category;
import com.gpt5.laundry.entity.Staff;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, String>, JpaSpecificationExecutor<Staff> {
}
