package com.gocoin.api.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Webhook {
    
    public static final String EVENT_INVOICE_CREATED = "invoice_created";
    public static final String EVENT_PAYMENT_RECEIVED = "invoice_payment_received";
    public static final String EVENT_READY_TO_SHIP = "invoice_ready_to_ship";
    //payment received after time window. Contact support support@gocoin.com
    public static final String EVENT_MERCHANT_REVIEW = "invoice_merchant_review";
    public static final String EVENT_INVOICE_INVALID = "invoice_invalid";

    @JsonProperty("id")
    private final String id;
    @JsonProperty("event")
    private final String event;
    @JsonProperty("payload")
    private final Invoice invoice;
}
