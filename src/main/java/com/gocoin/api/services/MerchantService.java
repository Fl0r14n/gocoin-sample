package com.gocoin.api.services;

import java.util.Collection;

import com.gocoin.api.resources.Merchant;
import com.gocoin.api.resources.Token;

/**
 * A service that knows how to perform operations for merchants
 */
public interface MerchantService {

    Collection<Merchant> getMerchants(Token t);

    Merchant getMerchant(Token t, String merchantId);

    Merchant addMerchant(Token t, Merchant m);

    Merchant updateMerchant(Token t, Merchant m);

    boolean deleteMerchant(Token t, Merchant m);
}
