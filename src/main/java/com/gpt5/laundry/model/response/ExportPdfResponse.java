package com.gpt5.laundry.model.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;

@Builder(toBuilder = true)
@Getter
public class ExportPdfResponse {
    private InputStreamResource isr;
    private HttpHeaders headers;
}
