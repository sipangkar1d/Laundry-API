package com.gpt5.laundry.service;

import com.gpt5.laundry.entity.Activity;
import com.gpt5.laundry.model.response.ActivityResponse;

import java.util.List;

public interface ActivityService {
    Activity create(Activity activity);
    List<ActivityResponse> getAll();
}
