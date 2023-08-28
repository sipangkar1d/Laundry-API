package com.gpt5.laundry.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class TwilioConfig {
    @Value("${laundry.twilio.account-sid}")
    private String accountSid;
    @Value("${laundry.twilio.auth-token}")
    private String authToken;
    @Value("${laundry.twilio.from-number}")
    private String fromPhoneNumber;
}

