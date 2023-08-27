package com.gpt5.laundry.service;

import com.gpt5.laundry.entity.Transaction;
import com.gpt5.laundry.model.request.TransactionFilterRequest;
import com.gpt5.laundry.model.request.TransactionRequest;
import com.gpt5.laundry.model.response.ExportToPdfResponse;
import com.gpt5.laundry.model.response.TransactionResponse;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface TransactionService {
    TransactionResponse create(TransactionRequest request);

    Transaction findById(String id);

    TransactionResponse findByIdResponse(String id);

    Page<TransactionResponse> getAll(TransactionFilterRequest request);

    void setIsPaid(String id);

    void updateStatus(String id);

    ExportToPdfResponse getTransactionForExportPdf(HttpServletResponse response,TransactionFilterRequest request) throws IOException;
}
