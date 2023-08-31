package com.gpt5.laundry.model.response;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Setter
@AllArgsConstructor
public class TransactionPdfExporter {
    private List<TransactionResponse> responseList;

    public void writeTableHeader(PdfPTable table) {
        String[] columnHeaders = {
                "No", "Customer Name", "Invoice", "Transaction Date",
                "Is Paid", "Status", "Category", "Price",
                "Product extra", "Quantity", "Price", "Sub Total"
        };

        float[] columnWidths = new float[columnHeaders.length];
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);

        for (int i = 0; i < columnHeaders.length; i++) {
            PdfPCell cell = new PdfPCell(new Phrase(columnHeaders[i], font));
            columnWidths[i] = cell.getPhrase().getFont().getBaseFont().getWidthPoint(columnHeaders[i], font.getSize()) + 5;
        }

        table.setTotalWidth(columnWidths);

        for (String columnHeader : columnHeaders) {
            PdfPCell cell = new PdfPCell(new Phrase(columnHeader, font));
            cell.setBackgroundColor(new Color(221, 230, 237));
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

    }

    public void writeTableData(PdfPTable table) {
        Long no = 1L;
        Long revenue = 0L;
        for (TransactionResponse response : responseList) {
            for (TransactionDetailResponse detailResponse : response.getTransactionDetailResponses()) {
                table.addCell(String.valueOf(no));
                table.addCell(response.getCustomerName());
                table.addCell(response.getInvoice());
                table.addCell(response.getDate());
                table.addCell(response.getIsPaid() ? "Lunas" : "Belum Dibayar");
                table.addCell(response.getStatus());
                table.addCell(detailResponse.getCategoryName());
                table.addCell(String.valueOf(detailResponse.getPrice()));
                table.addCell(detailResponse.getProductName());
                table.addCell(String.valueOf(detailResponse.getProductQuantity()));
                table.addCell(String.valueOf(detailResponse.getProductPrice()));
                table.addCell(String.valueOf(detailResponse.getSubTotal()));
                no++;
                revenue += detailResponse.getSubTotal();
            }
        }
        PdfPCell totalRevenue = new PdfPCell(new Phrase("Total",FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
        totalRevenue.setColspan(11);
        totalRevenue.setPadding(5);
        table.addCell(totalRevenue);
        table.addCell(String.valueOf(revenue));
    }

    public ExportPdfResponse export(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        document.open();
        Paragraph header = new Paragraph();

        Font companyFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Font.BOLD, new Color(0, 0, 255));
        Chunk companyNameChunk = new Chunk("GPT5 Laundry", companyFont);
        header.add(companyNameChunk);

        header.add(Chunk.NEWLINE);

        Font addressFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Chunk addressChunk = new Chunk("Jl. H Dahlan, South Jakarta", addressFont);
        header.add(addressChunk);

        header.add(Chunk.NEWLINE);

        Chunk emailChunk = new Chunk("Email: gpt5.laundry@gmail.com", addressFont);
        header.add(emailChunk);

        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);

        Paragraph separatorParagraph = new Paragraph();
        separatorParagraph.add(new Chunk(new LineSeparator(2, 100, null, Element.ALIGN_CENTER, -10)));
        document.add(separatorParagraph);

        document.add(Chunk.NEWLINE);

        Font contentFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Paragraph content = new Paragraph("Laundry Data", contentFont);
        content.setAlignment(Element.ALIGN_CENTER);
        document.add(content);

        PdfPTable table = new PdfPTable(12);
        table.setWidthPercentage(100);
        table.setSpacingBefore(15);

        writeTableHeader(table);
        writeTableData(table);
        document.add(table);

        document.close();

        DateFormat format = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String currentDate = format.format(new Date());
        String fileName = "transaction" + currentDate + ".pdf";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", fileName);

        ByteArrayInputStream bis = new ByteArrayInputStream(baos.toByteArray());
        InputStreamResource isr = new InputStreamResource(bis);

        return ExportPdfResponse.builder()
                .headers(headers)
                .isr(isr)
                .build();
    }
}
