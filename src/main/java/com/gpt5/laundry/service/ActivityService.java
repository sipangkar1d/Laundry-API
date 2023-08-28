package com.gpt5.laundry.service;

import com.gpt5.laundry.entity.Activity;

import java.util.List;

public interface ActivityService {
    Activity create(Activity activity);
    List<Activity> getAll();
}
