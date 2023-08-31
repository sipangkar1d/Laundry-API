package com.gpt5.laundry.controller;

import com.gpt5.laundry.entity.Category;
import com.gpt5.laundry.model.request.CategoryRequest;
import com.gpt5.laundry.model.response.CategoryResponse;
import com.gpt5.laundry.model.response.CommonResponse;
import com.gpt5.laundry.model.response.PagingResponse;
import com.gpt5.laundry.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody CategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .message("Success create category")
                        .statusCode(HttpStatus.CREATED.value())
                        .data(categoryService.create(request))
                        .build());
    }

    @GetMapping("")
    public ResponseEntity<?> getAll(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "sort-by", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size
    ) {
        Page<CategoryResponse> responses = categoryService.getAll(keyword, sortBy, direction, page, size);
        PagingResponse paging = PagingResponse.builder()
                .page(page)
                .size(size)
                .totalPages(responses.getTotalPages())
                .count(responses.getTotalElements())
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("get all category")
                        .data(responses.getContent())
                        .paging(paging)
                        .build());
    }

    @PutMapping()
    public ResponseEntity<?> update(@RequestBody CategoryRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("Success update category")
                        .statusCode(HttpStatus.OK.value())
                        .data(categoryService.update(request))
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        categoryService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("success delete category")
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id){
        CategoryResponse response = categoryService.getByIdResponse(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .data(response)
                        .message("success delete category")
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }
}
