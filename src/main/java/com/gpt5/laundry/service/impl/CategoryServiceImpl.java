package com.gpt5.laundry.service.impl;

import com.gpt5.laundry.entity.Category;
import com.gpt5.laundry.entity.CategoryPrice;
import com.gpt5.laundry.model.request.CategoryRequest;
import com.gpt5.laundry.model.response.CategoryResponse;
import com.gpt5.laundry.repository.CategoryRepository;
import com.gpt5.laundry.service.CategoryPriceService;
import com.gpt5.laundry.service.CategoryService;
import com.gpt5.laundry.util.ValidationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.metadata.facets.Validatable;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryPriceService categoryPriceService;
    private final ValidationUtils validationUtils;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public CategoryResponse create(CategoryRequest request) {
        log.info("start create category");
        validationUtils.validate(request);

        Category category = categoryRepository.saveAndFlush(
                Category.builder()
                        .name(request.getName())
                        .description(request.getDescription())
                        .build());

        CategoryPrice categoryPrice = categoryPriceService.create(
                CategoryPrice.builder()
                        .price(request.getPrice())
                        .isActive(true)
                        .category(category)
                        .build());

        category.setCategoryPrices(List.of(categoryPrice));
        categoryRepository.save(category);

        log.info("end create category");
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .price(categoryPrice.getPrice())
                .build();
    }

    @Override
    public Category getById(String id) {
        log.info("start get category by id");

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "category not found"));

        log.info("end get category by id");
        return category;
    }

    @Override
    public Page<CategoryResponse> getAll(String keyword, String sortBy, String direction, Integer page, Integer size) {
        log.info("start get all category");
        Specification<Category> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!keyword.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword + "%"));
            }
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
        Sort sorting = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sorting);

        Page<CategoryResponse> categoryResponses = categoryRepository.findAll(specification, pageable)
                .map(category -> {
                    List<CategoryPrice> prices = categoryPriceService.getAllIsActiveByCategory_Id(category.getId());
                    return CategoryResponse.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .description(category.getDescription())
                            .price(prices.get(0).getPrice())
                            .build();
                });
        log.info("end get all category");

        return categoryResponses;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public CategoryResponse update(CategoryRequest request) {
        log.info("start update category");
        validationUtils.validate(request);

        Category category = getById(request.getId());
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Optional<CategoryPrice> first = categoryPriceService.getAllIsActiveByCategory_Id(category.getId()).stream().findFirst();

        if (first.isEmpty()) {
            CategoryPrice categoryPrice = categoryPriceService.create(
                    CategoryPrice.builder()
                            .price(request.getPrice())
                            .isActive(true)
                            .category(category)
                            .build());
            category.addCategoryPrice(categoryPrice);
        }
        if (first.isPresent() && !first.get().getPrice().equals(request.getPrice())) {
            first.get().setIsActive(false);
            CategoryPrice categoryPrice = categoryPriceService.create(
                    CategoryPrice.builder()
                            .price(request.getPrice())
                            .isActive(true)
                            .category(category)
                            .build());
            category.addCategoryPrice(categoryPrice);
        }

        categoryRepository.save(category);

        log.info("end update category");
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .price(request.getPrice())
                .build();
    }

    @Override
    public void deleteById(String id) {
        log.info("start delete category");
        Category category = getById(id);
        categoryRepository.delete(category);
        log.info("end delete category");
    }
}
