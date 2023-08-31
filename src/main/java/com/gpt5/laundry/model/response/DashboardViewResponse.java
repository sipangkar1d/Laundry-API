package com.gpt5.laundry.model.response;

import com.gpt5.laundry.entity.Activity;
import lombok.*;

import java.util.List;

@Builder(toBuilder = true)
@Getter
public class DashboardViewResponse {
    private Integer customerTotal;
    private Long revenueTotal;
    private List<ActivityResponse> activities;
}
