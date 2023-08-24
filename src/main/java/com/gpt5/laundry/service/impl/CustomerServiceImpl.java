package com.gpt5.laundry.service.impl;

import com.gpt5.laundry.entity.Customer;
import com.gpt5.laundry.repository.CustomerRepository;
import com.gpt5.laundry.service.CustomerService;
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
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public Customer create(Customer customer) {
        log.info("start create customer");

        Customer save = customerRepository.save(customer);

        log.info("end create customer");
        return save;
    }

    @Override
    public Customer update(Customer request) {
        log.info("start update customer");
        getById(request.getId());
        Customer save = customerRepository.save(request);
        log.info("end update customer");
        return save;
    }

    @Override
    public Customer getById(String id) {
        log.info("start get customer by id");
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not Found"));
        log.info("end get customer by id");
        return customer;
    }

    @Override
    public Page<Customer> getAll(String keyword, Integer page, Integer size, String sortBy, String direction) {
        log.info("start get all customer");

        Specification<Customer> specification = (root, query, criteriaBuilder) -> {
            if (!keyword.isEmpty()) {
                Predicate predicate = criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), keyword.toLowerCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), keyword + "%")
                );
                return query.where(predicate).getRestriction();
            }
            return query.where().getRestriction();
        };
        Sort sorting = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sorting);

        Page<Customer> customerPage = customerRepository.findAll(specification, pageable);

        log.info("end get all customer");
        return customerPage;
    }
}
