package com.gocoin.api.services;

import com.gocoin.api.resources.Invoice;
import com.gocoin.api.resources.InvoiceList;
import com.gocoin.api.http.SearchQueryBuilder;
import com.gocoin.api.resources.Token;

/**
 * A service that knows how to perform operations for invoices
 */
public interface InvoiceService {

    InvoiceList searchInvoices(Token t, SearchQueryBuilder criteria);

    Invoice getInvoice(Token t, String invoiceId);

    Invoice createInvoice(Token t, String merchantId, Invoice i);
}
