package com.gocoin.api.services.impl;

import com.gocoin.api.GoCoin;
import com.gocoin.api.http.GCHttpRequest;
import com.gocoin.api.resources.Invoice;
import com.gocoin.api.resources.InvoiceList;
import com.gocoin.api.http.SearchQueryBuilder;
import com.gocoin.api.resources.Token;

import com.gocoin.api.services.InvoiceService;
import com.gocoin.api.http.JsonMarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTTPInvoiceService implements InvoiceService {
    
    private static final Logger L = LoggerFactory.getLogger(HTTPInvoiceService.class);

    @Override
    public InvoiceList searchInvoices(Token t, SearchQueryBuilder criteria) {
        GCHttpRequest client = GoCoin.getHTTPClient();
        client.setRequestOption(GCHttpRequest.KEY_OPTION_PATH, "/invoices/search");
        client.addAuthorizationHeader(t);
        //add the search criteria
        client.setRequestParameters(criteria.build());
        if (GoCoin.DEBUG) {
            L.debug("[DEBUG]: SEARCH PARAMETERS:\n" + criteria.build());
        }
        try {
            client.get(client.createURL(GCHttpRequest.URL_TYPE_API));
            client.checkResponse();
            return JsonMarshaller.read(client.getResponse(), InvoiceList.class);
        } catch (Exception e) {
            L.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Invoice getInvoice(Token t, String invoiceId) {
        GCHttpRequest client = GoCoin.getHTTPClient();
        client.setRequestOption(GCHttpRequest.KEY_OPTION_PATH, "/invoices/" + invoiceId);
        client.addAuthorizationHeader(t);
        try {
            client.get(client.createURL(GCHttpRequest.URL_TYPE_API));
            client.checkResponse();
            return JsonMarshaller.read(client.getResponse(), Invoice.class);
        } catch (Exception e) {
            L.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Invoice createInvoice(Token t, String merchantId, Invoice i) {
        GCHttpRequest client = GoCoin.getHTTPClient();
        client.setRequestOption(GCHttpRequest.KEY_OPTION_PATH, "/merchants/" + merchantId + "/invoices");
        client.setRequestOption(GCHttpRequest.KEY_OPTION_METHOD, GCHttpRequest.METHOD_POST);
        client.addAuthorizationHeader(t);
        try {
            client.setRequestBody(JsonMarshaller.write(i));
            client.post(client.createURL(GCHttpRequest.URL_TYPE_API));
            client.checkResponse();
            return JsonMarshaller.read(client.getResponse(), Invoice.class);
        } catch (Exception e) {
            L.error(e.getMessage());
            return null;
        }
    }
}
