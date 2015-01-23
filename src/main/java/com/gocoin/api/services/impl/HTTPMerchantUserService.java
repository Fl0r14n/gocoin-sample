package com.gocoin.api.services.impl;

import java.util.Collection;
import com.gocoin.api.GoCoin;
import com.gocoin.api.http.GCHttpRequest;
import com.gocoin.api.resources.Token;
import com.gocoin.api.resources.User;

import com.gocoin.api.services.MerchantUserService;
import com.gocoin.api.http.JsonMarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTTPMerchantUserService implements MerchantUserService {
    
    private static final Logger L = LoggerFactory.getLogger(HTTPMerchantUserService.class);

    @Override
    public Collection<User> getMerchantUsers(Token t, String merchantId) {
        GCHttpRequest client = GoCoin.getHTTPClient();
        client.setRequestOption(GCHttpRequest.KEY_OPTION_PATH, "/merchants/" + merchantId + "/users");
        client.addAuthorizationHeader(t);
        try {
            client.get(client.createURL(GCHttpRequest.URL_TYPE_API));
            client.checkResponse();
            return JsonMarshaller.readArray(client.getResponse(), User.class);
        } catch (Exception e) {
            L.error(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean addMerchantUser(Token t, String merchantId, User u) {
        return false;
    }

    @Override
    public boolean deleteMerchantUser(Token t, String merchantId, User u) {
        return false;
    }
}
