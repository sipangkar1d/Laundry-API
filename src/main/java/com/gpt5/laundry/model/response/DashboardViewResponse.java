package com.gpt5.laundry.model.response;

import com.gpt5.laundry.entity.Activity;
import lombok.Builder;
import lombok.Setter;

import java.util.List;

@Builder(toBuilder = true)
@Setter
public class DashboardViewResponse {
    private Integer customerTotal;
    private Long revenueTotal;
    private List<Activity> activities;
}
