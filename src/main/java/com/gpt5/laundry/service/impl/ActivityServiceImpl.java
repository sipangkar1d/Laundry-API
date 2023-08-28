package com.gpt5.laundry.service.impl;

import com.gpt5.laundry.entity.Activity;
import com.gpt5.laundry.repository.ActivityRepository;
import com.gpt5.laundry.service.ActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRepository activityRepository;

    @Override
    public Activity create(Activity activity) {
        log.info("start create activity");
        Activity saved = activityRepository.saveAndFlush(activity);
        log.info("end create activity");
        return saved;
    }

    @Override
    public List<Activity> getAll() {
        log.info("start get activities");
        Sort sorting = Sort.by(Sort.Direction.fromString("asc"), "activityTime");
        Pageable pageable = PageRequest.of(0, 6, sorting);
        List<Activity> activities = activityRepository.findAll(pageable).getContent();
        log.info("end get activities");
        return activities;
    }
}
