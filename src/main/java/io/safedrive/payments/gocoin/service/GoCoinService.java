package io.safedrive.payments.gocoin.service;

import com.gocoin.api.resources.Invoice;
import com.gocoin.api.resources.InvoiceList;
import com.gocoin.api.resources.Webhook;
import java.util.Map;

public interface GoCoinService {
    Invoice createInvoice(Invoice invoice);
    InvoiceList listInvoices(Map<String, String> requestParams);
    Invoice readInvoice(String id);
    String exchangeRate(String crypto, String fiat);
    void webhook(Webhook webhook);
}
