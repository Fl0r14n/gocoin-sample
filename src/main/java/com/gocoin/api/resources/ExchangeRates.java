package com.gocoin.api.resources;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import java.util.Date;
import lombok.Data;

/**
 * {
 * "timestamp":"2013-12-11T11:25:33.043Z", "prices":{"BTC":{"USD":"889.11"}} }
 */
@Data
public class ExchangeRates {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ExchangeRates.DATE_FORMAT, timezone = "UTC")
    private Date serverTs;
    @JsonProperty("prices")
    private Map<String, Map<String, String>> exchangeRates;

    public String getExchangeRate(String key, String exchange) {
        if (this.exchangeRates != null && this.exchangeRates.containsKey(key)) {
            Map<String, String> exchanges = this.exchangeRates.get(key);
            if (exchanges.containsKey(exchange)) {
                return exchanges.get(exchange);
            }
        }
        return null;
    }
}
