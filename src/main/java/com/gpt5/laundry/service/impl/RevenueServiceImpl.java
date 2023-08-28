package com.gpt5.laundry.service.impl;

import com.gpt5.laundry.entity.Revenue;
import com.gpt5.laundry.repository.RevenueRepository;
import com.gpt5.laundry.service.RevenueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RevenueServiceImpl implements RevenueService {
    private final RevenueRepository revenueRepository;

    @Override
    public Revenue create(Revenue request) {
        log.info("start create revenue");
        Revenue revenue = revenueRepository.saveAndFlush(request);
        log.info("end create revenue");
        return revenue;
    }

    @Override
    public Long getTotalRevenueThisMonth() {
        log.info("start get total revenue");
        Specification<Revenue> specification = (root, query, criteriaBuilder) -> {
            Integer month = LocalDate.now().getMonthValue();
            Integer year = LocalDate.now().getYear();

            Path<Date> transactionDatePath = root.get("paidDate");
            Expression<Integer> transactionMonth = criteriaBuilder.function("MONTH", Integer.class, transactionDatePath);
            Expression<Integer> transactionYear = criteriaBuilder.function("YEAR", Integer.class, transactionDatePath);

            List<Predicate> predicates = new ArrayList<>();
            Predicate monthPredicate = criteriaBuilder.equal(transactionMonth, month);
            Predicate yearPredicate = criteriaBuilder.equal(transactionYear, year);

            predicates.add(monthPredicate);
            predicates.add(yearPredicate);

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Long sum = revenueRepository.findAll(specification).stream()
                .filter(revenue -> revenue.getTransaction().getIsPaid())
                .mapToLong(Revenue::getRevenue).sum();

        log.info("end get total revenue");
        return sum;
    }
}
