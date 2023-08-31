package com.gpt5.laundry.controller;

import com.gpt5.laundry.model.request.TransactionFilterRequest;
import com.gpt5.laundry.model.request.TransactionRequest;
import com.gpt5.laundry.model.response.CommonResponse;
import com.gpt5.laundry.model.response.ExportPdfResponse;
import com.gpt5.laundry.model.response.PagingResponse;
import com.gpt5.laundry.model.response.TransactionResponse;
import com.gpt5.laundry.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

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
    public ResponseEntity<?> getAllFinishTransaction(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size
    ) {

        TransactionFilterRequest request = TransactionFilterRequest.builder()
                .keyword(keyword)
                .page(page)
                .size(size)
                .build();

        Page<TransactionResponse> responses = transactionService.getAllFinishTransaction(request);
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

    @GetMapping("/active")
    public ResponseEntity<?> getAllActiveTransaction(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "size") Integer size,
            @RequestParam(name = "page") Integer page
    ) {
        TransactionFilterRequest request = TransactionFilterRequest.builder()
                .keyword(keyword)
                .page(page)
                .size(size)
                .build();
        Page<TransactionResponse> activeTransaction = transactionService.getAllActiveTransaction(request);
        PagingResponse paging = PagingResponse.builder()
                .page(page)
                .size(size)
                .count(activeTransaction.getTotalElements())
                .totalPages(activeTransaction.getTotalPages())
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("get active transaction")
                        .data(activeTransaction.getContent())
                        .paging(paging)
                        .build());
    }

    @PutMapping("set-paid/{id}")
    public ResponseEntity<?> setIsPaid(@PathVariable String id) {
        transactionService.setIsPaid(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("set was paid by id")
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @PutMapping("update-status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable String id) {
        transactionService.updateStatus(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("update status by id")
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @GetMapping("/export")
    public ResponseEntity<?> exportToPDF(
            @RequestParam(name = "size") Integer size,
            @RequestParam(name = "page") Integer page,
            HttpServletResponse response) throws IOException {
        TransactionFilterRequest request = TransactionFilterRequest.builder()
                .size(size)
                .page(page)
                .build();

        ExportPdfResponse exportPdf = transactionService.exportToPdf(response, request);
        return new ResponseEntity<>(exportPdf.getIsr(), exportPdf.getHeaders(), HttpStatus.OK);
    }
}
