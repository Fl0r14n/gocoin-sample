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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoCoinServiceImpl implements GoCoinService {

    private static final Logger L = LoggerFactory.getLogger(GoCoinServiceImpl.class);

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
        //TODO add order_id, customer_name, item_name at least to identify user and purchase and do filtering
        signInvoice(invoice);
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
        if (webhook.getInvoice() != null && isValidInvoice(webhook.getInvoice())) {
            //TODO store invoice status for user id in the database
            L.info("============Webhook hit==============");
            try {
                L.info(JsonMarshaller.write(webhook));
            } catch (JsonProcessingException ex) {
                L.error(ex.getMessage());
            }
            Invoice invoice = webhook.getInvoice();
            switch (webhook.getEvent()) {
                case Webhook.EVENT_PAYMENT_RECEIVED: {
                    if (invoice.getStatus().equals(Invoice.STATUS_PAID)) {
                        //Mark Order Payment as Pending or Similar Status
                    }
                    break;
                }
                case Webhook.EVENT_INVOICE_INVALID: {
                    // Mark Order Payment as Failed
                    break;
                }
                case Webhook.EVENT_READY_TO_SHIP: {
                    // Mark Order Payment as Completed
                    break;
                }
            }
        }
    }

    //--------------------------------------------------------------------------
    private void signInvoice(Invoice invoice) {
        invoice.setUserDefined8(hashInvoice(invoice));
    }

    private boolean isValidInvoice(Invoice invoice) {
        return isValidHash(invoice.getUserDefined8(), hashInvoice(invoice));
    }

    private String hashInvoice(Invoice invoice) {
        return hash(invoice.getBasePrice(), invoice.getBasePriceCurrency(), invoice.getBasePriceCurrency(), "some_random_value");
    }

    private String hash(String... vals) {
        StringBuilder buf = new StringBuilder();
        for (String val : vals) {
            buf.append(val);
        }
        try {
            return DatatypeConverter.printHexBinary(MessageDigest.getInstance("SHA-1").digest(buf.toString().getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }

    private boolean isValidHash(String actualHexString, String expectedHexString) {
        return MessageDigest.isEqual(DatatypeConverter.parseHexBinary(actualHexString), DatatypeConverter.parseHexBinary(expectedHexString));
    }
}
