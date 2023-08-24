package com.gpt5.laundry.service.impl;

import com.gpt5.laundry.entity.Status;
import com.gpt5.laundry.entity.constant.EStatus;
import com.gpt5.laundry.repository.StatusRepository;
import com.gpt5.laundry.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {
    private final StatusRepository statusRepository;

    @Override
    public Status getOrSave(EStatus status) {
        return statusRepository.findByStatus(status)
                .orElseGet(() -> statusRepository.save(Status.builder().status(status).build()));
    }
}
