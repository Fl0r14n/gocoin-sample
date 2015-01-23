package com.gocoin.api.services.impl;

import java.util.Collection;
import com.gocoin.api.GoCoin;
import com.gocoin.api.http.GCHttpRequest;
import com.gocoin.api.resources.Merchant;
import com.gocoin.api.resources.Token;

import com.gocoin.api.services.MerchantService;
import com.gocoin.api.http.JsonMarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTTPMerchantService implements MerchantService {
    
    private static final Logger L = LoggerFactory.getLogger(HTTPMerchantService.class);

    @Override
    public Collection<Merchant> getMerchants(Token t) {
        GCHttpRequest client = GoCoin.getHTTPClient();
        client.setRequestOption(GCHttpRequest.KEY_OPTION_PATH, "/merchants/");
        client.addAuthorizationHeader(t);
        try {
            client.get(client.createURL(GCHttpRequest.URL_TYPE_API));
            client.checkResponse();
            return JsonMarshaller.readArray(client.getResponse(), Merchant.class);
        } catch (Exception e) {
            L.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Merchant getMerchant(Token t, String merchantId) {
        GCHttpRequest client = GoCoin.getHTTPClient();
        client.setRequestOption(GCHttpRequest.KEY_OPTION_PATH, "/merchants/" + merchantId);
        client.addAuthorizationHeader(t);
        try {
            client.get(client.createURL(GCHttpRequest.URL_TYPE_API));
            client.checkResponse();
            return JsonMarshaller.read(client.getResponse(), Merchant.class);
        } catch (Exception e) {
            L.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Merchant addMerchant(Token t, Merchant m) {
        GCHttpRequest client = GoCoin.getHTTPClient();
        client.setRequestOption(GCHttpRequest.KEY_OPTION_PATH, "/merchants");
        client.setRequestOption(GCHttpRequest.KEY_OPTION_METHOD, GCHttpRequest.METHOD_POST);
        client.addAuthorizationHeader(t);
        try {
            client.setRequestBody(JsonMarshaller.write(m));
            client.post(client.createURL(GCHttpRequest.URL_TYPE_API));
            client.checkResponse();
            return JsonMarshaller.read(client.getResponse(), Merchant.class);
        } catch (Exception e) {
            L.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Merchant updateMerchant(Token t, Merchant m) {
        GCHttpRequest client = GoCoin.getHTTPClient();
        client.setRequestOption(GCHttpRequest.KEY_OPTION_PATH, "/merchants/" + m.getId());
        client.setRequestOption(GCHttpRequest.KEY_OPTION_METHOD, GCHttpRequest.METHOD_PUT);
        client.addAuthorizationHeader(t);
        try {
            client.setRequestBody(JsonMarshaller.write(m));
            client.put(client.createURL(GCHttpRequest.URL_TYPE_API));
            client.checkResponse();
            return JsonMarshaller.read(client.getResponse(), Merchant.class);
        } catch (Exception e) {
            L.error(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean deleteMerchant(Token t, Merchant m) {
        GCHttpRequest client = GoCoin.getHTTPClient();
        client.setRequestOption(GCHttpRequest.KEY_OPTION_PATH, "/merchants/" + m.getId());
        client.setRequestOption(GCHttpRequest.KEY_OPTION_METHOD, GCHttpRequest.METHOD_DELETE);
        client.addAuthorizationHeader(t);
        try {
            client.delete(client.createURL(GCHttpRequest.URL_TYPE_API));
            client.checkResponse();
            return true;
        } catch (Exception e) {
            L.error(e.getMessage());
            return false;
        }
    }
}
