package com.gpt5.laundry.repository;

import com.gpt5.laundry.entity.Status;
import com.gpt5.laundry.entity.constant.EStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, String> {
    Optional<Status> findByStatus(EStatus role);
}
