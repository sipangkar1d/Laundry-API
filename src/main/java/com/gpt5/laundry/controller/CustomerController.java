package com.gpt5.laundry.controller;

import com.gpt5.laundry.entity.Customer;
import com.gpt5.laundry.model.request.CustomerRequest;
import com.gpt5.laundry.model.response.CommonResponse;
import com.gpt5.laundry.model.response.PagingResponse;
import com.gpt5.laundry.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody CustomerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("success create customer")
                        .data(customerService.create(request))
                        .build());
    }

    @PutMapping()
    public ResponseEntity<?> update(@RequestBody CustomerRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("success update customer")
                        .data(customerService.update(request))
                        .build());
    }

    @GetMapping()
    public ResponseEntity<?> getAll(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "keyword", defaultValue = "0") Integer page,
            @RequestParam(name = "keyword", defaultValue = "5") Integer size,
            @RequestParam(name = "keyword", defaultValue = "name") String sortBy,
            @RequestParam(name = "keyword", defaultValue = "asc") String direction) {
        Page<Customer> customer = customerService.getAll(keyword, page, size, sortBy, direction);
        PagingResponse paging = PagingResponse.builder()
                .page(page)
                .size(size)
                .totalPages(customer.getTotalPages())
                .count(customer.getTotalElements())
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("success get all customer")
                        .data(customer.getContent())
                        .paging(paging)
                        .build());
    }

    @GetMapping("/{phone}")
    public ResponseEntity<?> getByPhone(@PathVariable String phone) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("success get customer by phone")
                        .statusCode(HttpStatus.OK.value())
                        .data(customerService.getByPhone(phone))
                        .build());
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<?> getById(@PathVariable String id) {
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(CommonResponse.builder()
//                        .message("success get customer by phone")
//                        .statusCode(HttpStatus.OK.value())
//                        .data(customerService.getByPhone(id))
//                        .build());
//    }

}
