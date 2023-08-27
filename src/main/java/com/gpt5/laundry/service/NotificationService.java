package com.gpt5.laundry.service;

import com.gpt5.laundry.model.request.NotificationRequest;
import com.twilio.rest.api.v2010.account.Message;

public interface NotificationService {
    Message sendNotification(NotificationRequest request);
}
