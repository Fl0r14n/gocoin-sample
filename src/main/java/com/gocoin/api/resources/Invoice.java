package com.gocoin.api.resources;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URL;
import java.util.Date;
import lombok.Data;

//{
//            "id": "84c4fc04-66f2-49a5-a12a-36baf7f9f450",
//            "status": "unpaid",
//            "payment_address": "1xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
//            "price": "1.00000000",
//            "crypto_balance_due": "1.00000000",
//            "price_currency": "BTC",
//            "valid_bill_payment_currencies": null,
//            "base_price": "600.00",
//            "base_price_currency": "USD",
//            "service_fee_rate": "0.01",
//            "usd_spot_rate": "1.0",
//            "spot_rate": "0.001666667",
//            "inverse_spot_rate": "600.0",
//            "crypto_payout_split": "100",
//            "confirmations_required": 2,
//            "crypto_url": null,
//            "gateway_url": "https://gateway.gocoin.com/invoices/84c4fc04-66f2-49a5-a12a-36baf7f9f450",
//            "notification_level": null,
//            "redirect_url": "http://www.example.com/redirect",
//            "order_id": null,
//            "refund_address": null,
//            "item_name": null,
//            "item_sku": null,
//            "item_description": null,
//            "physical": null,
//            "customer_name": null,
//            "customer_address_1": null,
//            "customer_address_2": null,
//            "customer_city": null,
//            "customer_region": null,
//            "customer_country": null,
//            "customer_postal_code": null,
//            "customer_email": null,
//            "customer_phone": null,
//            "user_defined_1": null,
//            "user_defined_2": null,
//            "user_defined_3": null,
//            "user_defined_4": null,
//            "user_defined_5": null,
//            "user_defined_6": null,
//            "user_defined_7": null,
//            "user_defined_8": null,
//            "data": null,
//            "expires_at": "2013-10-01T18:47:45.150Z",
//            "created_at": "2013-10-01T18:32:45.153Z",
//            "updated_at": "2013-10-01T18:32:45.153Z",
//            "server_time": "2014-06-24T19:03:16Z",
//            "callback_url": "https://www.example.com/gocoin/callback",
//            "merchant_id": "63d3cd4c-1514-11e3-a3f0-080027fd9579"
//}
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Invoice {
    
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    @JsonProperty("id")
    private String id;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("payment_address")
    private String paymentAddress;
    
    @JsonProperty("price")
    private Double price;
    
    @JsonProperty("crypto_balance_due")
    private String cryptoBalanceDue;
    
    @JsonProperty("price_currency")
    private String priceCurrency;
    
    @JsonProperty("valid_bill_payment_currencies")
    private String validBillPaymentCurrencies;
    
    @JsonProperty("base_price")
    private String basePrice;
    
    @JsonProperty("base_price_currency")
    private String basePriceCurrency;
    
    @JsonProperty("service_fee_rate")
    private Double serviceFeeRate;
    
    @JsonProperty("usd_spot_rate")
    private Double spotRateUSD;
    
    @JsonProperty("spot_rate")
    private Double spotRate;
    
    @JsonProperty("inverse_spot_rate")
    private Double spotRateInverse;
    
    @JsonProperty("crypto_payout_split")
    private Integer cryptoPayoutSplit;
    
    @JsonProperty("confirmations_required")
    private Integer confiramtions;
    
    @JsonProperty("crypto_url")
    private String cryptoUrl;
    
    @JsonProperty("gateway_url")
    private URL gatewayURL;
    
    @JsonProperty("redirect_url")
    private URL redirectUrl;
    
    @JsonProperty("callback_url")
    private URL callbackUrl;
    
    @JsonProperty("notification_level")
    private String notificationLevel;
    
    @JsonProperty("order_id")
    private String orderId;
    
    @JsonProperty("refund_address")
    private String refundAddress;
    
    @JsonProperty("item_name")
    private String itemName;
    
    @JsonProperty("item_sku")
    private String itemSku;
    
    @JsonProperty("item_description")
    private String itemDescription;
    
    @JsonProperty("physical")
    private String physical;
    
    @JsonProperty("customer_name")
    private String customerName;
    
    @JsonProperty("customer_address_1")
    private String customerAddress1;
    
    @JsonProperty("customer_address_2")
    private String customerAddress2;
    
    @JsonProperty("customer_city")
    private String customerCity;
    
    @JsonProperty("customer_region")
    private String customerRegion;
    
    @JsonProperty("customer_country")
    private String customerCountry;
    
    @JsonProperty("customer_postal_code")
    private String customerPostalCode;
    
    @JsonProperty("customer_email")
    private String customerEmail;
    
    @JsonProperty("customer_phone")
    private String customerPhone;
    
    @JsonProperty("user_defined_1")
    private String userDefined1;
    
    @JsonProperty("user_defined_2")
    private String userDefined2;
    
    @JsonProperty("user_defined_3")
    private String userDefined3;
    
    @JsonProperty("user_defined_4")
    private String userDefined4;
    
    @JsonProperty("user_defined_5")
    private String userDefined5;
    
    @JsonProperty("user_defined_6")
    private String userDefined6;
    
    @JsonProperty("user_defined_7")
    private String userDefined7;
    
    @JsonProperty("user_defined_8")
    private String userDefined8;
    
    @JsonProperty("data")
    private Object data;
    
    @JsonProperty("created_at")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=Invoice.DATE_FORMAT, timezone="UTC")
    private Date createdAt;    
    
    @JsonProperty("updated_at")    
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=Invoice.DATE_FORMAT, timezone="UTC")
    private Date updatedAt;
    
    @JsonProperty("expires_at")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=Invoice.DATE_FORMAT, timezone="UTC")
    private Date expiresAt;
    
    @JsonProperty("server_time")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=Invoice.DATE_FORMAT, timezone="UTC")
    private Date serverTs;
    
    @JsonProperty("merchant_id")
    private String merchantId;
}
