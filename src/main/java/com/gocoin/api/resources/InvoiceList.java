package com.gocoin.api.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;
import lombok.Data;

@Data
public class InvoiceList {

    @Data
    public static class PagingInfo {

        @JsonProperty("total")
        private int total;
        @JsonProperty("page")
        private int page;
        @JsonProperty("per_page")
        private int perPage;
    }

    @JsonProperty("invoices")
    private Collection<Invoice> invoices;
    @JsonProperty("status")
    private String status;
    @JsonProperty("paging_info")
    private PagingInfo pagingInfo;
}
