package com.gpt5.laundry.service;

import com.gpt5.laundry.entity.Status;
import com.gpt5.laundry.entity.constant.EStatus;

public interface StatusService {
    Status getOrSave(EStatus status);
}
