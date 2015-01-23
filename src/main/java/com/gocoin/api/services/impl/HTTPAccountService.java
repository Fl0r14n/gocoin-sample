package com.gocoin.api.services.impl;

import java.util.Collection;

import com.gocoin.api.GoCoin;
import com.gocoin.api.http.GCHttpRequest;
import com.gocoin.api.resources.Account;
import com.gocoin.api.resources.Token;

import com.gocoin.api.services.AccountService;
import com.gocoin.api.http.JsonMarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTTPAccountService implements AccountService {
    
    private static final Logger L = LoggerFactory.getLogger(HTTPAccountService.class);

    @Override
    public Collection<Account> getAccounts(Token t, String merchantId) {
        GCHttpRequest client = GoCoin.getHTTPClient();
        client.setRequestOption(GCHttpRequest.KEY_OPTION_PATH, "/merchants/" + merchantId + "/accounts");
        client.addAuthorizationHeader(t);
        try {
            client.get(client.createURL(GCHttpRequest.URL_TYPE_API));
            client.checkResponse();
            return JsonMarshaller.readArray(client.getResponse(), Account.class);
        } catch (Exception e) {
            L.error(e.getMessage());
            return null;
        }
    }
}
