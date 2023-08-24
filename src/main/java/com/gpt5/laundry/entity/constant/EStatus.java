package com.gpt5.laundry.entity.constant;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public enum EStatus {
    PROCESS,
    PENDING,
    FINISHED;

    public static EStatus get(String value) {
        for (EStatus eStatus : EStatus.values()) {
            if (eStatus.name().equalsIgnoreCase(value)) return eStatus;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "role not found");
    }
}
