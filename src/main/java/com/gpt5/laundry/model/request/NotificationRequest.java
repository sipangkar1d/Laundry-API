package com.gpt5.laundry.model.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class NotificationRequest {
    private String toPhoneNumber;
    private String message;
}
