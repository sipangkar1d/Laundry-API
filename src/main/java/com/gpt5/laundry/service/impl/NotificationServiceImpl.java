package com.gpt5.laundry.service.impl;

import com.gpt5.laundry.config.TwilioConfig;
import com.gpt5.laundry.model.request.NotificationRequest;
import com.gpt5.laundry.service.NotificationService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final TwilioConfig twilioConfig;


    @Override
    public Message sendNotification(NotificationRequest request) {
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
        String getMessage = request.getMessage();

        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("whatsapp:+62" + request.getToPhoneNumber()),
                new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),
                getMessage

        ).create();
//        Message message = Message.creator(
//                        new com.twilio.type.PhoneNumber("+62" + request.getToPhoneNumber()),
//                        new com.twilio.type.PhoneNumber("+18507882151"),
//                        "Laundry GPT-5\n"+getMessage
//                )
//                .create();
        log.info(message.getBody());
        return message;
    }
}
