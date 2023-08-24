package com.gpt5.laundry.service.impl;

import com.gpt5.laundry.entity.ProductPrice;
import com.gpt5.laundry.repository.ProductPriceRepository;
import com.gpt5.laundry.service.ProductPriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductPriceServiceImpl implements ProductPriceService {
    private final ProductPriceRepository productPriceRepository;

    @Override
    public ProductPrice create(ProductPrice productPrice) {
        log.info("start create product price");
        try {
            ProductPrice saved = productPriceRepository.saveAndFlush(productPrice);
            log.info("end create product price");
            return saved;
        } catch (ConstraintViolationException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "price can not be negative");
        }
    }

    @Override
    public ProductPrice getById(String id) {
        log.info("start get product price by id");
        ProductPrice productPrice = productPriceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product price not found"));
        log.info("end get product price by id");
        return productPrice;
    }

    @Override
    public void deactivate(ProductPrice productPrice) {
        log.info("start deactivate product price by id");
        getById(productPrice.getId());
        productPrice.setIsActive(false);
        productPriceRepository.save(productPrice);
        log.info("end deactivate product price by id");
    }

    @Override
    public List<ProductPrice> getProductPriceIsActiveAndProduct_Id(String id) {
        log.info("start get product price is active");
        List<ProductPrice> productPrices = productPriceRepository.findAllByIsActiveTrueAndProduct_Id(id);
        log.info("end get product price is active");
        return productPrices;
    }
}
