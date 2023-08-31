package com.gpt5.laundry.controller;

import com.gpt5.laundry.entity.Product;
import com.gpt5.laundry.model.request.ProductRequest;
import com.gpt5.laundry.model.response.CommonResponse;
import com.gpt5.laundry.model.response.PagingResponse;
import com.gpt5.laundry.model.response.ProductResponse;
import com.gpt5.laundry.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .message("success create product")
                        .statusCode(HttpStatus.CREATED.value())
                        .data(productService.create(request))
                        .build());
    }

    @GetMapping()
    public ResponseEntity<?> getAll(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "sort-by", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction) {

        Page<ProductResponse> responses = productService.getAll(keyword, page, size, sortBy, direction);
        PagingResponse paging = PagingResponse.builder()
                .page(page)
                .size(size)
                .totalPages(responses.getTotalPages())
                .count(responses.getTotalElements())
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Get all product")
                        .data(responses.getContent())
                        .paging(paging)
                        .build());
    }

    @PutMapping()
    public ResponseEntity<?> update(@RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("success update product")
                        .statusCode(HttpStatus.OK.value())
                        .data(productService.update(request))
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        productService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("success delete product")
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        ProductResponse response = productService.getByIdResponse(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("success delete product")
                        .statusCode(HttpStatus.OK.value())
                        .data(response)
                        .build());
    }
}
