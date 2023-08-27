package com.gpt5.laundry.service.impl;

import com.gpt5.laundry.entity.*;
import com.gpt5.laundry.entity.constant.EStatus;
import com.gpt5.laundry.model.request.TransactionFilterRequest;
import com.gpt5.laundry.model.request.TransactionRequest;
import com.gpt5.laundry.model.response.ExportToPdfResponse;
import com.gpt5.laundry.model.response.TransactionDetailResponse;
import com.gpt5.laundry.model.response.TransactionPdfExporter;
import com.gpt5.laundry.model.response.TransactionResponse;
import com.gpt5.laundry.repository.TransactionRepository;
import com.gpt5.laundry.service.*;
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

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionDetailService transactionDetailService;
    private final CustomerService customerService;
    private final CategoryPriceService categoryPriceService;
    private final CategoryService categoryService;
    private final ProductPriceService productPriceService;
    private final ProductService productService;
    private final StatusService statusService;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public TransactionResponse create(TransactionRequest request) {
        log.info("start transaction");
        Customer customer = customerService.getByPhone(request.getCustomerPhone());
        String invoice = createInvoice();

        Transaction transaction = transactionRepository.saveAndFlush(
                Transaction.builder()
                        .customer(customer)
                        .isPaid(request.getIsPaid())
                        .status(statusService.getOrSave(EStatus.PROCESS))
                        .invoice(invoice)
                        .build());

        List<TransactionDetail> transactionDetails = request.getTransactionDetailRequests().stream().map(transactionDetailRequest -> {
            Category category = categoryService.getById(transactionDetailRequest.getCategoryId());
            List<CategoryPrice> categoryPrices = categoryPriceService.getAllIsActiveByCategory_Id(category.getId());

            Product product = productService.getById(transactionDetailRequest.getProductId());
            if (product.getStock() < transactionDetailRequest.getProductQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "stock not found");
            }

            product.setStock(product.getStock() - transactionDetailRequest.getProductQuantity());
            productService.updateStock(product.getId(), product.getStock());

            List<ProductPrice> productPrices = productPriceService.getProductPriceIsActiveAndProduct_Id(product.getId());


            return transactionDetailService.create(
                    TransactionDetail.builder()
                            .categoryPrice(categoryPrices.get(0))
                            .quantity(transactionDetailRequest.getQuantity())
                            .productPrice(productPrices.get(0))
                            .productQuantity(transactionDetailRequest.getProductQuantity())
                            .transaction(transaction)
                            .build());
        }).collect(Collectors.toList());

        transaction.setTransactionDetails(transactionDetails);
        transactionRepository.save(transaction);

        log.info("end transaction");

        return getTransactionResponse(transaction);
    }


    @Override
    public Transaction findById(String id) {
        log.info("start get transaction by id");

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "transaction not found"));

        log.info("end get transaction by id");
        return transaction;
    }

    @Override
    public TransactionResponse findByIdResponse(String id) {
        log.info("start get transaction by id");

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "transaction not found"));

        log.info("end get transaction by id");
        return getTransactionResponse(transaction);
    }

    @Override
    public Page<TransactionResponse> getAll(TransactionFilterRequest request) {
        log.info("start get all transaction");

        Sort sorting = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sorting);

        Specification<Transaction> specification = getTransactionSpecification(request);

        Page<TransactionResponse> transactionResponses = transactionRepository.findAll(specification, pageable)
                .map(TransactionServiceImpl::getTransactionResponse);

        log.info("end get all transaction");
        return transactionResponses;
    }


    @Override
    public void setIsPaid(String id) {
        log.info("start set is paid");

        Transaction transaction = findById(id);
        transaction.setIsPaid(true);
        transactionRepository.save(transaction);

        log.info("end set is paid");
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateStatus(String id) {
        log.info("start set is paid");
        Transaction transaction = findById(id);

        if (transaction.getStatus().getStatus().name().equals(EStatus.PENDING.name())) {
            if (transaction.getIsPaid()) {
                transaction.setStatus(statusService.getOrSave(EStatus.FINISHED));
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "transaction cannot be completed before it is paid.");
            }
        } else {
            transaction.setStatus(statusService.getOrSave(EStatus.PENDING));
        }

        transactionRepository.save(transaction);
        log.info("end set is paid");
    }

    @Override
    public ExportToPdfResponse getTransactionForExportPdf(HttpServletResponse response, TransactionFilterRequest request) throws IOException {
        Specification<Transaction> transactionSpecification = getTransactionSpecification(request);
        Sort sort = Sort.by(Sort.Direction.DESC, "isPaid", "status");
        List<Transaction> transactions = transactionRepository.findAll(transactionSpecification, sort);

        TransactionPdfExporter exporter = new TransactionPdfExporter(transactions.stream().map(TransactionServiceImpl::getTransactionResponse)
                .collect(Collectors.toList()));

        return exporter.export(response);
    }

    private Integer countTransactionDay() {
        Specification<Transaction> specification = (root, query, criteriaBuilder) -> {
            LocalDate date = LocalDate.now();
            List<Predicate> predicates = List.of(criteriaBuilder.equal(criteriaBuilder
                    .function("DATE", LocalDate.class, root.get("transactionDate")), date));

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
        return transactionRepository.findAll(specification).size();
    }

    private String createInvoice() {
        String prefix = "GPT";
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = date.format(formatter);
        Integer suffix = countTransactionDay() + 1;
        return String.format("%s-%s-%d", prefix, formattedDate, suffix);
    }

    private static TransactionResponse getTransactionResponse(Transaction transaction) {
        List<TransactionDetailResponse> transactionDetailResponses = transaction.getTransactionDetails().stream().map(transactionDetail ->
                TransactionDetailResponse.builder()
                        .categoryName(transactionDetail.getCategoryPrice().getCategory().getName())
                        .price(transactionDetail.getCategoryPrice().getPrice())
                        .quantity(transactionDetail.getQuantity())
                        .productName(transactionDetail.getProductPrice().getProduct().getName())
                        .productPrice(transactionDetail.getProductPrice().getPrice())
                        .productQuantity(transactionDetail.getProductQuantity())
                        .subTotal(transactionDetail.getCategoryPrice().getPrice()
                                + (transactionDetail.getProductPrice().getPrice() * transactionDetail.getProductQuantity()))
                        .build()).collect(Collectors.toList());

        Long grandTotal = transactionDetailResponses.stream().mapToLong(TransactionDetailResponse::getSubTotal).sum();
        return TransactionResponse.builder()
                .id(transaction.getId())
                .customerName(transaction.getCustomer().getName())
                .invoice(transaction.getInvoice())
                .date(transaction.getTransactionDate().toString())
                .isPaid(transaction.getIsPaid())
                .status(transaction.getStatus().getStatus().name())
                .transactionDetailResponses(transactionDetailResponses)
                .grandTotal(grandTotal)
                .build();
    }

    private static Specification<Transaction> getTransactionSpecification(TransactionFilterRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getKeyword() != null) {
                predicates.add(criteriaBuilder.like(root.get("invoice"), request.getKeyword()));
            }
            if (request.getMonth() != null && request.getYear() != null) {
                Path<Date> transactionDatePath = root.get("transactionDate");
                Expression<Integer> transactionMonth = criteriaBuilder.function("MONTH", Integer.class, transactionDatePath);
                Expression<Integer> transactionYear = criteriaBuilder.function("YEAR", Integer.class, transactionDatePath);

                Predicate monthPredicate = criteriaBuilder.equal(transactionMonth, request.getMonth());
                Predicate yearPredicate = criteriaBuilder.equal(transactionYear, request.getYear());

                predicates.add(monthPredicate);
                predicates.add(yearPredicate);
            }

            if (request.getDay() != null) {
                Path<Date> transactionDatePath = root.get("transactionDate");
                Expression<Integer> transactionDay = criteriaBuilder.function("DAY", Integer.class, transactionDatePath);
                Predicate dayPredicate = criteriaBuilder.equal(transactionDay, request.getDay());

                predicates.add(dayPredicate);
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
