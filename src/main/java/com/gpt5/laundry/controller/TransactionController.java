package com.gpt5.laundry.controller;

import com.gpt5.laundry.model.request.TransactionFilterRequest;
import com.gpt5.laundry.model.request.TransactionRequest;
import com.gpt5.laundry.model.response.CommonResponse;
import com.gpt5.laundry.model.response.PagingResponse;
import com.gpt5.laundry.model.response.TransactionResponse;
import com.gpt5.laundry.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody TransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .message("transaction created")
                        .statusCode(HttpStatus.CREATED.value())
                        .data(transactionService.create(request))
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .message("transaction created")
                        .statusCode(HttpStatus.CREATED.value())
                        .data(transactionService.findByIdResponse(id))
                        .build());
    }

    @GetMapping()
    public ResponseEntity<?> getAll(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "sort-by", defaultValue = "invoice") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "day", required = false) Integer day,
            @RequestParam(name = "month", required = false) Integer month,
            @RequestParam(name = "year", required = false) Integer year
    ) {

        TransactionFilterRequest request = TransactionFilterRequest.builder()
                .keyword(keyword)
                .sortBy(sortBy)
                .direction(direction)
                .page(page)
                .size(size)
                .day(day)
                .month(month)
                .year(year)
                .build();

        Page<TransactionResponse> responses = transactionService.getAll(request);
        PagingResponse paging = PagingResponse.builder()
                .page(page)
                .size(size)
                .totalPages(responses.getTotalPages())
                .count(responses.getTotalElements())
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("get all transaction")
                        .statusCode(HttpStatus.OK.value())
                        .data(responses.getContent())
                        .paging(paging)
                        .build());
    }

    @PostMapping("set-paid/{id}")
    public ResponseEntity<?> setIsPaid(@PathVariable String id) {
        transactionService.setIsPaid(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("set was paid by id")
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @PostMapping("update-status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable String id) {
        transactionService.updateStatus(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("update status by id")
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @GetMapping("/export")
    public ResponseEntity<?> exportToPDF(HttpServletResponse response) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder().build());
    }
}
