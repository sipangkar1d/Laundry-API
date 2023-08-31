package com.gpt5.laundry.controller;

import com.gpt5.laundry.entity.Activity;
import com.gpt5.laundry.model.response.*;
import com.gpt5.laundry.service.ActivityService;
import com.gpt5.laundry.service.CustomerService;
import com.gpt5.laundry.service.RevenueService;
import com.gpt5.laundry.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/dashboard")
public class DashboardViewController {
    private final RevenueService revenueService;
    private final ActivityService activityService;
    private final CustomerService customerService;
    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<?> getDashboardViewData(
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "page", defaultValue = "0") Integer page
    ) {
        Long totalRevenueThisMonth = revenueService.getTotalRevenueThisMonth();
        Integer countCustomer = customerService.countCustomer();
        List<ActivityResponse> activityResponses = activityService.getAll();

        DashboardViewResponse response = DashboardViewResponse.builder()
                .customerTotal(countCustomer)
                .revenueTotal(totalRevenueThisMonth)
                .activities(activityResponses)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("get dashboard view data")
                        .statusCode(HttpStatus.OK.value())
                        .data(response)
                        .build());
    }
}
