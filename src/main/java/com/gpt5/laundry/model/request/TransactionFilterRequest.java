package com.gpt5.laundry.model.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class TransactionFilterRequest {
    private String keyword;
    private String sortBy;
    private String direction;
    private Integer page;
    private Integer size;
    private Integer month;
    private Integer year;
}
