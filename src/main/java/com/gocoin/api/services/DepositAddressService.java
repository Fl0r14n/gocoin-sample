package com.gocoin.api.services;

import com.gocoin.api.resources.Token;

/**
 * A service that knows how to perform operations for deposit addresses
 */
public interface DepositAddressService {

    boolean generateDepositAddress(Token t, String accountId);
}
