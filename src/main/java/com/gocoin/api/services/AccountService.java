package com.gocoin.api.services;

import java.util.Collection;

import com.gocoin.api.resources.Account;
import com.gocoin.api.resources.Token;

/**
 * A service that knows how to perform operations for accounts
 */
public interface AccountService {

    Collection<Account> getAccounts(Token t, String merchantId);
}
