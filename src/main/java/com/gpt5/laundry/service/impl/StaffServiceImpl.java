package com.gpt5.laundry.service.impl;

import com.gpt5.laundry.entity.Staff;
import com.gpt5.laundry.model.request.StaffRequest;
import com.gpt5.laundry.model.response.StaffResponse;
import com.gpt5.laundry.repository.StaffRepository;
import com.gpt5.laundry.service.StaffService;
import com.gpt5.laundry.service.UserCredentialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StaffServiceImpl implements StaffService {
    private final StaffRepository staffRepository;
    private final UserCredentialService userCredentialService;

    @Override
    public Staff create(Staff request) {
        log.info("start create staff");

        Staff staff = staffRepository.saveAndFlush(request);

        log.info("end create staff");
        return staff;
    }

    @Override
    public Staff getById(String id) {
        log.info("start get staff by id");

        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "staff not found"));

        log.info("end get staff by id");
        return staff;
    }

    @Override
    public Page<StaffResponse> getAll(String keyword, Integer page, Integer size, String sortBy, String direction) {
        log.info("start get all staff");

        Specification<Staff> specification = (root, query, criteriaBuilder) -> {
            if (!keyword.isEmpty()) {
                Predicate predicate = criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), keyword.toLowerCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), keyword.toLowerCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), keyword + "%")
                );
                return query.where(predicate).getRestriction();
            }

            return query.where().getRestriction();
        };

        Sort sorting = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sorting);
        Page<StaffResponse> staffResponses = staffRepository
                .findAll(specification, pageable)
                .map(staff -> StaffResponse.builder()
                        .id(staff.getId())
                        .name(staff.getName())
                        .email(staff.getEmail())
                        .phone(staff.getPhone())
                        .address(staff.getAddress())
                        .build());

        log.info("start get all staff");
        return staffResponses;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public StaffResponse update(StaffRequest request) {
        log.info("start deactivate staff");

        Staff staff = getById(request.getId());

        staff.setName(request.getName());
        staff.setEmail(request.getEmail());
        staff.setAddress(request.getAddress());
        staff.setPhone(request.getPhone());
        Staff save = staffRepository.save(staff);

        log.info("end deactivate staff");
        return StaffResponse.builder()
                .id(staff.getId())
                .name(save.getName())
                .email(save.getEmail())
                .phone(save.getPhone())
                .address(save.getAddress())
                .build();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteById(String id) {
        log.info("start deactivate staff");

        Staff staff = getById(id);
        userCredentialService.deactivate(staff.getEmail());

        log.info("end deactivate staff");
    }
}
