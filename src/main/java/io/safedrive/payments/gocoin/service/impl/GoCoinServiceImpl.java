package io.safedrive.payments.gocoin.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gocoin.api.services.InvoiceService;
import com.gocoin.api.GoCoin;
import com.gocoin.api.Scope;
import com.gocoin.api.http.JsonMarshaller;
import com.gocoin.api.http.SearchQueryBuilder;
import com.gocoin.api.resources.ExchangeRates;
import com.gocoin.api.resources.Invoice;
import com.gocoin.api.resources.InvoiceList;
import com.gocoin.api.resources.Token;
import com.gocoin.api.resources.Webhook;
import com.gocoin.api.services.UserService;
import io.safedrive.payments.gocoin.service.GoCoinService;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.jboss.logging.Logger;

public class GoCoinServiceImpl implements GoCoinService {
    
    private static final Logger L = Logger.getLogger(GoCoinServiceImpl.class);

    private final InvoiceService invoiceService = GoCoin.getInvoiceService();

    private final UserService userService = GoCoin.getUserService();
    
    public GoCoinServiceImpl(String apiKey, String merchantId) {
        this.token = new Token(apiKey, Scope.getScope(Scope.INVOICE_READ_WRITE, Scope.USER_READ));
        this.merchantId = merchantId;
    }
    private final Token token;
    private String merchantId;
    
    @PostConstruct
    public void postContruct() {
        if (null == merchantId || merchantId.isEmpty()) {
            this.merchantId = userService.getResourceOwner(token).getMerchantId();
        }
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        return invoiceService.createInvoice(token, merchantId, invoice);
    }

    @Override
    public InvoiceList listInvoices(Map<String, String> requestParams) {
        return invoiceService.searchInvoices(token, new SearchQueryBuilder(requestParams));
    }

    @Override
    public Invoice readInvoice(String id) {
        return invoiceService.getInvoice(token, id);
    }

    @Override
    public String exchangeRate(String crypto, String fiat) {
        ExchangeRates rates = GoCoin.getExchangeRates();
        return rates.getExchangeRate(crypto, fiat != null ? fiat : "USD");
    }

    @Override
    public void webhook(Webhook webhook) {
        //TODO store invoice status for user id in the database
        try {
            L.debug(JsonMarshaller.write(webhook));
        } catch (JsonProcessingException ex) {
            L.error(ex.getMessage());
        }
    }
}
