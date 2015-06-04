package io.safedrive.payments.gocoin.controller;

import com.gocoin.api.resources.Invoice;
import com.gocoin.api.resources.InvoiceList;
import com.gocoin.api.resources.Webhook;
import io.safedrive.payments.gocoin.service.GoCoinService;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping(value = "api/v1/payment/gocoin", produces = MediaType.APPLICATION_JSON_VALUE)
public class GoCoinController {
    
    @Autowired
    private GoCoinService gcService;
    
    /*
     {
     "price_currency": "BTC",                                     
     "base_price": 600.00,                                        
     "base_price_currency": "USD",
     "order_id": "1",
     "customer_email": "john@somewhere.com"
     "cusomer_name": "John_Doe",
     "item_name": "soda",
     "item_sku": "12345"
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
        return new ResponseEntity<>(gcService.createInvoice(invoice), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public @ResponseBody
    InvoiceList listInvoices(@RequestParam Map<String, String> requestParams) {
        return gcService.listInvoices(requestParams);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/")
    public @ResponseBody
    Invoice readInvoice(@PathVariable String id) {
        return gcService.readInvoice(id);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/exchange/")
    public @ResponseBody
    String exchangeRate(@RequestParam(value = "crypto") String crypto, @RequestParam(value = "fiat", required = false) String fiat) {
        return gcService.exchangeRate(crypto, fiat);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/webhook/")
    public @ResponseBody
    void webhook(@RequestBody Webhook webhook) {
        gcService.webhook(webhook);
    }
}
