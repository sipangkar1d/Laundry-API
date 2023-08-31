package com.gpt5.laundry.service.impl;

import com.gpt5.laundry.entity.Activity;
import com.gpt5.laundry.model.response.ActivityResponse;
import com.gpt5.laundry.repository.ActivityRepository;
import com.gpt5.laundry.service.ActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<ActivityResponse> getAll() {
        log.info("start get activities");

        Sort sorting = Sort.by(Sort.Direction.fromString("asc"), "activityTime");
        Pageable pageable = PageRequest.of(0, 6, sorting);
        Specification<Activity> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Date now = new Date();

            java.sql.Date today = new java.sql.Date(now.getTime());

            Path<Date> transactionDatePath = root.get("activityTime");
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(transactionDatePath, today));

            java.sql.Date tomorrow = new java.sql.Date(now.getTime() + 24 * 60 * 60 * 1000);
            predicates.add(criteriaBuilder.lessThan(transactionDatePath, tomorrow));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        List<ActivityResponse> activityResponses = activityRepository.findAll(specification,pageable)
                .getContent().stream().map(activity ->
                        ActivityResponse.builder()
                                .description(activity.getDescription())
                                .time(getDifferentTime(activity.getActivityTime()))
                                .build()
                ).collect(Collectors.toList());

        log.info("end get activities");

        return activityResponses;
    }

    private static String getDifferentTime(Date startDate){
        Date currentDate = new Date();

        long timeDifferenceMillis = currentDate.getTime() - startDate.getTime();

        long hours = timeDifferenceMillis / (60 * 60 * 1000);
        long minutes = (timeDifferenceMillis % (60 * 60 * 1000)) / (60 * 1000);

        String result = "";

        if (hours > 0) {
            result += hours + "h ";
        }

        if (minutes > 0 || hours == 0) {
            result += minutes + "m";
        }

        return result + " ago";
    }

//    @Override
//    public List<Activity> getAll() {
//        log.info("start get activities");
//        Sort sorting = Sort.by(Sort.Direction.fromString("asc"), "activityTime");
//        Pageable pageable = PageRequest.of(0, 6, sorting);
//
//        Specification<Activity> specification = (root, query, criteriaBuilder) -> {
//            List<Predicate> predicates = new ArrayList<>();
//
//            LocalDate today = LocalDate.now();
//
//            Path<LocalDate> transactionDatePath = root.get("activityTime");
//            predicates.add(criteriaBuilder.equal(transactionDatePath, today));
//
//
//            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
//        };
//
//        List<Activity> activities = activityRepository.findAll(specification, pageable).getContent();
//        log.info("test " + activities.size());
//        log.info("end get activities");
//        return activities;
//    }
}
