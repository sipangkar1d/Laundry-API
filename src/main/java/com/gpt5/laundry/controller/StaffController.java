package com.gpt5.laundry.controller;

import com.gpt5.laundry.entity.Staff;
import com.gpt5.laundry.entity.UserCredential;
import com.gpt5.laundry.model.request.StaffRequest;
import com.gpt5.laundry.model.response.CommonResponse;
import com.gpt5.laundry.model.response.PagingResponse;
import com.gpt5.laundry.model.response.StaffResponse;
import com.gpt5.laundry.service.StaffService;
import com.gpt5.laundry.service.UserCredentialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/staff")
@Slf4j
public class StaffController {
    private final StaffService staffService;
    private final UserCredentialService userCredentialService;

    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getAll(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "sort_by", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction
    ) {

        Page<StaffResponse> staffResponses = staffService.getAll(keyword, page, size, sortBy, direction);

        PagingResponse paging = PagingResponse.builder()
                .page(page)
                .size(size)
                .totalPages(staffResponses.getTotalPages())
                .count(staffResponses.getTotalElements())
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("Get all staff")
                        .statusCode(HttpStatus.OK.value())
                        .data(staffResponses.getContent())
                        .paging(paging)
                        .build());
    }

    @PutMapping()
    @PreAuthorize("hasAnyRole('STAFF')")
    public ResponseEntity<?> update(@RequestBody StaffRequest request, Authentication authentication) {
        UserCredential userCredential = userCredentialService.getByEmail(authentication.getName());
        Staff staff = staffService.getById(request.getId());

        log.info(userCredential.getEmail() + staff.getEmail());
        if (!userCredential.getEmail().equalsIgnoreCase(staff.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "you do not have access to this resource");
        }


        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Success Update Staff")
                        .data(staffService.update(request))
                        .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable String id) {
        staffService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Success deactivate admin")
                        .build());
    }
}
