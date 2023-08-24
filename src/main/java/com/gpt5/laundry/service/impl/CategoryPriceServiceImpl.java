package com.gpt5.laundry.service.impl;

import com.gpt5.laundry.entity.CategoryPrice;
import com.gpt5.laundry.repository.CategoryPriceRepository;
import com.gpt5.laundry.service.CategoryPriceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CategoryPriceServiceImpl implements CategoryPriceService {
    private final CategoryPriceRepository categoryPriceRepository;

    @Override
    public CategoryPrice create(CategoryPrice categoryPrice) {
        log.info("start create category price");

        CategoryPrice save = categoryPriceRepository.saveAndFlush(categoryPrice);

        log.info("end create category price");
        return save;
    }

    @Override
    public CategoryPrice getById(String id) {
        log.info("start get category price by id");

        CategoryPrice categoryPrice = categoryPriceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "category price not found"));

        log.info("end get category price by id");

        return categoryPrice;
    }

    @Override
    public void deactivate(CategoryPrice categoryPrice) {
        log.info("start deactivate category price");

        categoryPrice.setIsActive(false);
        categoryPriceRepository.save(categoryPrice);

        log.info("end deactivate category price");
    }

    @Override
    public List<CategoryPrice> getAllIsActiveByCategory_Id(String id) {
        log.info("start get all category price by category id");

        List<CategoryPrice> categoryPrices = categoryPriceRepository.findAllByIsActiveTrueAndCategory_Id(id);

        log.info("end get all category price by category id");
        return categoryPrices;
    }
}
