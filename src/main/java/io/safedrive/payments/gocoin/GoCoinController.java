package io.safedrive.payments.gocoin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gocoin.api.GoCoin;
import com.gocoin.api.Scope;
import com.gocoin.api.http.JsonMarshaller;
import com.gocoin.api.http.SearchQueryBuilder;
import com.gocoin.api.resources.ExchangeRates;
import com.gocoin.api.resources.Invoice;
import com.gocoin.api.resources.InvoiceList;
import com.gocoin.api.resources.Token;
import com.gocoin.api.resources.Webhook;
import com.gocoin.api.services.InvoiceService;
import com.gocoin.api.services.UserService;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/payment/gocoin", produces = MediaType.APPLICATION_JSON_VALUE)
public class GoCoinController {

    private static final Logger L = LoggerFactory.getLogger(GoCoinController.class);

    private final InvoiceService invoiceService;
    private final UserService userService;

    public GoCoinController(String apiKey) {
        this(apiKey, null);
    }

    public GoCoinController(String apiKey, String merchantId) {
        invoiceService = GoCoin.getInvoiceService();
        userService = GoCoin.getUserService();
        token = new Token(apiKey, Scope.getScope(Scope.INVOICE_READ_WRITE, Scope.USER_READ));
        if (null != merchantId) {
            this.merchantId = merchantId;
        } else {
            this.merchantId = userService.getResourceOwner(token).getMerchantId();
        }
    }
    private final Token token;
    private final String merchantId;

    /*
     {
     "price_currency": "BTC",                                     
     "base_price": 600.00,                                        
     "base_price_currency": "USD",
     "order_id": "1",
     "cusomer_name": "John_Doe",
     "item_name": "soda"
     }
     */
    @RequestMapping(method = RequestMethod.POST, value = "/")
    public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice, HttpServletRequest request) {
        try {
            invoice.setCallbackUrl(new URL(request.getRequestURL().append("webhook/").toString()));
        } catch (MalformedURLException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //TODO do we need to set up redirect uri when user returns from gocoin paying form if using gateway_url?
        //TODO add order_id, customer_name, item_name at least to identify user and purchase and do filtering
        return new ResponseEntity<>(invoiceService.createInvoice(token, merchantId, invoice), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public @ResponseBody
    InvoiceList listInvoices(@RequestParam Map<String, String> requestParams) {
        return invoiceService.searchInvoices(token, new SearchQueryBuilder(requestParams));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/")
    public @ResponseBody
    Invoice readInvoice(@PathVariable String id) {
        return invoiceService.getInvoice(token, id);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/exchange/")
    public @ResponseBody
    String exchangeRate(@RequestParam(value = "crypto") String crypto, @RequestParam(value = "fiat", required = false) String fiat) {
        ExchangeRates rates = GoCoin.getExchangeRates();
        return rates.getExchangeRate(crypto, fiat != null ? fiat : "USD");
    }

    @RequestMapping(method = RequestMethod.POST, value = "/webhook/")
    public @ResponseBody
    void webhook(@RequestBody Webhook webhook) {
        //TODO store invoice status for user id in the database
        try {
            L.debug(JsonMarshaller.write(webhook));
        } catch (JsonProcessingException ex) {
            L.error(ex.getMessage());
        }
    }
}
