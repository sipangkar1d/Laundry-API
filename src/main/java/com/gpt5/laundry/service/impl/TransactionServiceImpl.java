package com.gpt5.laundry.service.impl;

import com.gpt5.laundry.entity.*;
import com.gpt5.laundry.entity.constant.EStatus;
import com.gpt5.laundry.model.request.NotificationRequest;
import com.gpt5.laundry.model.request.TransactionFilterRequest;
import com.gpt5.laundry.model.request.TransactionRequest;
import com.gpt5.laundry.model.response.ExportPdfResponse;
import com.gpt5.laundry.model.response.TransactionDetailResponse;
import com.gpt5.laundry.model.response.TransactionPdfExporter;
import com.gpt5.laundry.model.response.TransactionResponse;
import com.gpt5.laundry.repository.TransactionRepository;
import com.gpt5.laundry.service.*;
import com.gpt5.laundry.util.ValidationUtils;
import com.twilio.rest.api.v2010.account.Message;
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
import javax.persistence.criteria.Join;
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
    private final CategoryPriceService categoryPriceService;
    private final ProductPriceService productPriceService;
    private final NotificationService notificationService;
    private final CategoryService categoryService;
    private final CustomerService customerService;
    private final ActivityService activityService;
    private final RevenueService revenueService;
    private final ProductService productService;
    private final StatusService statusService;
    private final ValidationUtils validationUtils;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public TransactionResponse create(TransactionRequest request) {
        log.info("start transaction");
        validationUtils.validate(request);

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

        Message message = sendNotificationToCustomer(transaction, customer);
        if (message.getErrorCode() != null || message.getErrorMessage() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "any problem when send notification");
        }
        Activity activity = activityService.create(Activity.builder()
                .description(String.format("laundry %s masuk antrian", customer.getName()))
                .build());
        log.info("end transaction");
        return getTransactionResponse(transaction);
    }

    private Message sendNotificationToCustomer(Transaction transaction, Customer customer) {
        List<TransactionDetail> transactionDetails = transaction.getTransactionDetails();
        StringBuilder getDetailMessage = new StringBuilder();
        long total = 0L;

        for (TransactionDetail transactionDetail : transactionDetails) {
            getDetailMessage
                    .append(transactionDetail.getCategoryPrice().getCategory().getName())
                    .append(" @ Rp").append(transactionDetail.getCategoryPrice().getPrice()).append(",-\n")
                    .append("Ket: ").append(transactionDetail.getQuantity()).append(" pcs").append(",-\n")
                    .append(transactionDetail.getProductPrice().getProduct().getName())
                    .append(" @ Rp").append(transactionDetail.getProductPrice().getPrice()).append(",-\n")
                    .append("Ket: ").append(transactionDetail.getProductQuantity()).append(" pcs").append(",-\n");

            total += (transactionDetail.getCategoryPrice().getPrice() +
                    (transactionDetail.getProductPrice().getPrice() * transactionDetail.getProductQuantity()));
        }

        String remaining = transaction.getIsPaid() ? "Rp 0,-" : String.valueOf(total);
        String status = transaction.getIsPaid() ? "Lunas" : "Belum Lunas";
        return notificationService.sendNotification(
                NotificationRequest.builder()
                        .toPhoneNumber(customer.getPhone())
                        .message("*FAKTUR ELEKTRONIK*" + "\n"
                                + "GPT-5 Laundry" + "\n"
                                + "Jl. H Dahlan, South Jakarta 628234567890" + "\n"
                                + "\n"
                                + "Pelanggan Yth," + "\n"
                                + customer.getName() + "\n"
                                + "\n"
                                + "Invoice" + "\n"
                                + transaction.getInvoice() + "\n"
                                + "\n"
                                + "Terima:" + "\n"
                                + transaction.getTransactionDate().toString() + "\n"
                                + "\n"
                                + "=".repeat(20) + "\n"
                                + getDetailMessage
                                + "=".repeat(20) + "\n"
                                + "Detail biaya :" + "\n"
                                + "Total tagihan : " + total + "\n"
                                + "Sisa tagihan : " + remaining + "\n"
                                + "\n"
                                + "Status: " + status + "\n"
                                + "=".repeat(20) + "\n"
                                + "Syarat & ketentuan: " + "\n"
                                + "PERHATIAN:" + "\n"
                                + "JIKA MENERIMA PESAN NOTA INI, MAKA KONSUMEN DIANGGAP SETUJU DENGAN KETENTUAN LAYANAN GPT-5 LAUNDRY" + "\n"
                                + "\n"
                                + "Note : mohon konsumen untuk membaca ketentuan sebelum meninggalkan outlet kami"
                        ).build());
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
    public Page<TransactionResponse> getAllFinishTransaction(TransactionFilterRequest request) {
        log.info("start get all transaction finish");
        Sort sorting = Sort.by(Sort.Direction.fromString("asc"), "invoice", "isPaid");

        Specification<Transaction> transactionSpecification = finishStatus();

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sorting);

        Page<TransactionResponse> responsePage = transactionRepository.findAll(transactionSpecification, pageable).map(TransactionServiceImpl::getTransactionResponse);

        log.info("end get all transaction finish");

        return responsePage;
    }

    @Override
    public Page<TransactionResponse> getAllActiveTransaction(TransactionFilterRequest request) {
        log.info("start get all transaction active");
        Sort sorting = Sort.by(Sort.Direction.fromString("asc"), "invoice", "isPaid");

        Specification<Transaction> transactionSpecification = pendingOrProcessStatus();

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sorting);

        Page<TransactionResponse> responsePage = transactionRepository.findAll(transactionSpecification, pageable).map(TransactionServiceImpl::getTransactionResponse);

        log.info("end get all transaction active");
        return responsePage;
    }

    public static Specification<Transaction> hasStatus(EStatus status) {
        return (root, query, criteriaBuilder) -> {
            Join<Transaction, Status> statusJoin = root.join("status");
            Path<EStatus> statusPath = statusJoin.get("status");

            return criteriaBuilder.equal(statusPath, status);
        };
    }

    public static Specification<Transaction> finishStatus() {
        return hasStatus(EStatus.FINISHED);
    }

    public static Specification<Transaction> pendingOrProcessStatus() {
        return hasStatus(EStatus.PENDING)
                .or(hasStatus(EStatus.PROCESS));
    }


    @Override
    @Transactional(rollbackOn = Exception.class)
    public void setIsPaid(String id) {
        log.info("start set is paid");

        Transaction transaction = findById(id);

        Revenue revenue = revenueService.create(Revenue.builder()
                        .revenue(transaction.getTransactionDetails().stream().mapToLong(
                                transactionDetail -> transactionDetail.getCategoryPrice().getPrice()
                                        + (transactionDetail.getProductQuantity() * transactionDetail.getProductPrice().getPrice()))
                                .sum()).build());

        transaction.setIsPaid(true);
        transactionRepository.save(transaction);

        Activity activity = activityService.create(Activity.builder()
                .description(String.format("%s melakukan pembayaran", transaction.getCustomer().getName()))
                .build());
        log.info("end set is paid");
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateStatus(String id) {
        log.info("start set is paid");
        Transaction transaction = findById(id);

        if (transaction.getStatus().getStatus().equals(EStatus.PENDING)) {
            if (transaction.getIsPaid()) {
                transaction.setStatus(statusService.getOrSave(EStatus.FINISHED));
                Activity activity = activityService.create(Activity.builder()
                        .description(String.format("laundry %s sudah diambil", transaction.getCustomer().getName()))
                        .build());
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "transaction cannot be completed before it is paid.");
            }
        } else {
            transaction.setStatus(statusService.getOrSave(EStatus.PENDING));
            Message message = notificationService.sendNotification(
                    NotificationRequest.builder()
                            .toPhoneNumber(transaction.getCustomer().getPhone())
                            .message("*FAKTUR ELEKTRONIK*" + "\n"
                                    + "GPT-5 Laundry" + "\n"
                                    + "Jl. H Dahlan, South Jakarta 628234567890" + "\n"
                                    + "\n"
                                    + "Pelanggan Yth," + "\n"
                                    + transaction.getCustomer().getName() + "\n"
                                    + "\n"
                                    + "Invoice" + "\n"
                                    + transaction.getInvoice() + "\n"
                                    + "\n"
                                    + "*Informasi bahwa laundry anda sudah selesai dan sudah dapat diambil*"
                                    + "\n")
                            .build());
            Activity activity = activityService.create(Activity.builder()
                    .description(String.format("laundry %s selesai dan siap untuk diambil", transaction.getCustomer().getName()))
                    .build());
            if (message.getErrorCode() != null || message.getErrorMessage() != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "any problem when send notification");
            }
        }

        transactionRepository.save(transaction);
        log.info("end set is paid");
    }

    @Override
    public ExportPdfResponse exportToPdf(HttpServletResponse response, TransactionFilterRequest request) throws IOException {
        List<TransactionResponse> transactionResponses = getAllFinishTransaction(request).getContent();
        TransactionPdfExporter exporter = new TransactionPdfExporter(transactionResponses);
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

//    private static Specification<Transaction> getTransactionSpecification(TransactionFilterRequest request) {
//        return (root, query, criteriaBuilder) -> {
//            List<Predicate> predicates = new ArrayList<>();
//
//            if (request.getKeyword() != null) {
//                predicates.add(criteriaBuilder.like(root.get("invoice"), request.getKeyword()));
//            }
//            if (request.getMonth() != null && request.getYear() != null) {
//                Path<Date> transactionDatePath = root.get("transactionDate");
//                Expression<Integer> transactionMonth = criteriaBuilder.function("MONTH", Integer.class, transactionDatePath);
//                Expression<Integer> transactionYear = criteriaBuilder.function("YEAR", Integer.class, transactionDatePath);
//
//                Predicate monthPredicate = criteriaBuilder.equal(transactionMonth, request.getMonth());
//                Predicate yearPredicate = criteriaBuilder.equal(transactionYear, request.getYear());
//
//                predicates.add(monthPredicate);
//                predicates.add(yearPredicate);
//            }
//
//            if (request.getDay() != null) {
//                Path<Date> transactionDatePath = root.get("transactionDate");
//                Expression<Integer> transactionDay = criteriaBuilder.function("DAY", Integer.class, transactionDatePath);
//                Predicate dayPredicate = criteriaBuilder.equal(transactionDay, request.getDay());
//
//                predicates.add(dayPredicate);
//            }
//
//            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
//        };
//    }
}
