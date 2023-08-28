package com.gpt5.laundry.controller;

import com.gpt5.laundry.entity.Activity;
import com.gpt5.laundry.model.response.CommonResponse;
import com.gpt5.laundry.model.response.DashboardViewResponse;
import com.gpt5.laundry.service.ActivityService;
import com.gpt5.laundry.service.CustomerService;
import com.gpt5.laundry.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/dashboard")
public class DashboardViewController {
    private final RevenueService revenueService;
    private final ActivityService activityService;
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<?> getDashboardViewData() {
        Long totalRevenueThisMonth = revenueService.getTotalRevenueThisMonth();
        Integer countCustomer = customerService.countCustomer();
        List<Activity> activities = activityService.getAll();

        DashboardViewResponse response = DashboardViewResponse.builder()
                .customerTotal(countCustomer)
                .revenueTotal(totalRevenueThisMonth)
                .activities(activities)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("get dashboard view data")
                        .statusCode(HttpStatus.OK.value())
                        .data(response)
                        .build());
    }
}
