package com.gocoin.api.services;

import java.util.Collection;

import com.gocoin.api.resources.Token;
import com.gocoin.api.resources.User;

/**
 * A service that knows how to perform operations for merchant users
 */
public interface MerchantUserService {

    Collection<User> getMerchantUsers(Token t, String merchantId);

    boolean addMerchantUser(Token t, String merchantId, User u);

    boolean deleteMerchantUser(Token t, String merchantId, User u);
}
