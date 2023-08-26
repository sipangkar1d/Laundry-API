package com.gpt5.laundry.util;

import lombok.Getter;

@Getter
public class BadRequestUtils extends RuntimeException{
    private final Object errors;
    public BadRequestUtils(Object errors) {
        this.errors = errors;
    }

}


