package com.gocoin.api.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * {
 * "id":"e6634e90-8ae2-4fc1-97a2-262990c755f4", "currency_code":"BTC",
 * "balance":"0.00000000" }
 */
@Data
public class Account {

    @JsonProperty("id")
    private String id;
    @JsonProperty("currency_code")
    private String currency;
    @JsonProperty("balance")
    private String balance;
}
