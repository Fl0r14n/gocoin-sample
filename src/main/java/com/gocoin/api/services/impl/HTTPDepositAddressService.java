package com.gocoin.api.services.impl;

import com.gocoin.api.GoCoin;
import com.gocoin.api.http.GCHttpRequest;
import com.gocoin.api.resources.Token;

import com.gocoin.api.services.DepositAddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTTPDepositAddressService implements DepositAddressService {
    
    private static final Logger L = LoggerFactory.getLogger(HTTPDepositAddressService.class);

    @Override
    public boolean generateDepositAddress(Token t, String accountId) {
        GCHttpRequest client = GoCoin.getHTTPClient();
        client.setRequestOption(GCHttpRequest.KEY_OPTION_PATH, "/accounts/" + accountId + "/deposit_address");
        client.setRequestOption(GCHttpRequest.KEY_OPTION_METHOD, GCHttpRequest.METHOD_POST);
        client.addAuthorizationHeader(t);
        try {
            client.post(client.createURL(GCHttpRequest.URL_TYPE_API));
            client.checkResponse();
            return true;
        } catch (Exception e) {
            L.error(e.getMessage());
            return false;
        }
    }
}
