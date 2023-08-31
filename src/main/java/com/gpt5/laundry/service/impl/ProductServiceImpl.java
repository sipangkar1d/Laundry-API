package com.gpt5.laundry.service.impl;

import com.gpt5.laundry.entity.Product;
import com.gpt5.laundry.entity.ProductPrice;
import com.gpt5.laundry.model.request.ProductRequest;
import com.gpt5.laundry.model.response.ProductResponse;
import com.gpt5.laundry.repository.ProductRepository;
import com.gpt5.laundry.service.ProductPriceService;
import com.gpt5.laundry.service.ProductService;
import com.gpt5.laundry.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductPriceService productPriceService;
    private final ValidationUtils validationUtils;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ProductResponse create(ProductRequest request) {
        log.info("start create product");
        validationUtils.validate(request);

        Product product;
        ProductPrice productPrice;
        try {
            product = productRepository.saveAndFlush(
                    Product.builder()
                            .name(request.getName())
                            .stock(request.getStock())
                            .build());

            productPrice = productPriceService.create(
                    ProductPrice.builder()
                            .price(request.getPrice())
                            .product(product)
                            .isActive(true)
                            .build());

            product.setProductPrices(List.of(productPrice));
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Duplicate name of product");
        }

        log.info("end create product");
        return ProductResponse.builder()
                .id(product.getId())
                .stock(product.getStock())
                .name(product.getName())
                .price(productPrice.getPrice())
                .build();
    }

    @Override
    public Product getById(String id) {
        log.info("start get product by id");

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found"));

        log.info("end get product by id");
        return product;
    }

    @Override
    public ProductResponse getByIdResponse(String id) {
        log.info("start get product by id");

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found"));
        List<ProductPrice> productPrices = productPriceService.getProductPriceIsActiveAndProduct_Id(product.getId());

        log.info("end get product by id");
        return ProductResponse.builder()
                .id(product.getId())
                .stock(product.getStock())
                .name(product.getName())
                .price(productPrices.get(0).getPrice())
                .build();
    }

    @Override
    public Page<ProductResponse> getAll(String keyword, Integer page, Integer size, String sortBy, String direction) {
        log.info("start get all customer");

        Specification<Product> specification = (root, query, criteriaBuilder) -> {
            if (keyword != null) {
                Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%");
                return query.where(predicate).getRestriction();
            }
            return query.where().getRestriction();
        };
        Sort sorting = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sorting);

        Page<ProductResponse> productResponses = productRepository.findAll(specification, pageable)
                .map(product -> {
                    List<ProductPrice> productPriceIsActiveAndProductId = productPriceService.getProductPriceIsActiveAndProduct_Id(product.getId());
                    return ProductResponse.builder()
                            .name(product.getName())
                            .id(product.getId())
                            .stock(product.getStock())
                            .price(productPriceIsActiveAndProductId.get(0).getPrice())
                            .build();
                });
        log.info("end get all product");
        return productResponses;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ProductResponse update(ProductRequest request) {
        log.info("start update product");
        validationUtils.validate(request);

        Product product = getById(request.getId());
        product.setName(request.getName());
        product.setStock(request.getStock());

        Optional<ProductPrice> first = productPriceService.getProductPriceIsActiveAndProduct_Id(product.getId()).stream().findFirst();

        if (first.isEmpty()) {
            ProductPrice categoryPrice = productPriceService.create(
                    ProductPrice.builder()
                            .price(request.getPrice())
                            .isActive(true)
                            .product(product)
                            .build());
            product.addProductPrices(categoryPrice);
        }
        if (first.isPresent() && !first.get().getPrice().equals(request.getPrice())) {
            first.get().setIsActive(false);
            ProductPrice categoryPrice = productPriceService.create(
                    ProductPrice.builder()
                            .price(request.getPrice())
                            .isActive(true)
                            .product(product)
                            .build());
            product.addProductPrices(categoryPrice);
        }

        productRepository.save(product);

        log.info("end update product");
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .price(request.getPrice())
                .build();
    }

    @Override
    public void deleteById(String id) {
        log.info("start delete product");
        Product product = getById(id);
        productRepository.delete(product);
        log.info("end delete product");
    }

    @Override
    public void updateStock(String id, Integer stock) {
        Product product = getById(id);
        product.setStock(stock);
        productRepository.save(product);
    }
}
