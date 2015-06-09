package com.gocoin.api.resources;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URL;
import java.util.Date;
import lombok.Data;

//{"id":"53bb37ef-a65a-4cf2-a9c9-48b8bcb45191",
//"merchant_id":"f2fe3ccb-e509-4d58-b652-ede24636c422",
//"status":"unpaid",
//"payment_address":"3LkHke8wqTAXPXBEWpzE8UmhKJ3idpGLYz",
//"refund_address":null,
//"refund_token":"448fd0bf1efb00a0a280c8a5",
//"crypto_url":"bitcoin:3LkHke8wqTAXPXBEWpzE8UmhKJ3idpGLYz?amount=0.0501\u0026label=Payment%20to%20acme+inc",
//"gateway_url":"https://gateway.gocoin.com/invoices/53bb37ef-a65a-4cf2-a9c9-48b8bcb45191",
//"redirect_url":null,
//"callback_url":"http://localhost:8080/api/v1/payment/gocoin/webhook/",
//"base_price":"10.00",
//"price":"0.05010000",
//"crypto_balance_due":"0.0501",
//"crypto_payout_split":100,
//"service_fee_rate":0.01,
//"base_price_currency":"EUR",
//"price_currency":"BTC",
//"payout_currency":"USD",
//"base_price_currency_detail":{"id":"EUR","name":"Euro","symbol":"€","is_crypto":false,"sort_order":101,"precision":2,"aliases":[]},
//"price_currency_detail":{"id":"BTC","name":"Bitcoin","symbol":"Ƀ","is_crypto":true,"sort_order":1,"precision":8,"aliases":["XBT"]},
//"payout_currency_detail":{"id":"USD","name":"United States Dollar","symbol":"$","is_crypto":false,"sort_order":100,"precision":2,"aliases":[]},
//"spot_rate":"0.004438839948783406556620234303945198",
//"inverse_spot_rate":"225.284085828343313373253493014",
//"usd_spot_rate":"1.1296221849",
//"valid_bill_payment_currencies":null,
//"valid_bill_payment_currency_details":null,
//"merchant_review_reason":null,
//"merchant_review_gain_loss":null,
//"merchant_review_spot_rate":null,
//"order_id":null,
//"item_name":null,
//"item_sku":null,
//"item_description":null,
//"physical":null,
//"customer_name":null,
//"customer_address_1":null,
//"customer_address_2":null,
//"customer_city":null,
//"customer_region":null,
//"customer_country":null,
//"customer_postal_code":null,
//"customer_email":null,
//"customer_phone":null,
//"user_defined_1":null,
//"user_defined_2":null,
//"user_defined_3":null,
//"user_defined_4":null,
//"user_defined_5":null,
//"user_defined_6":null,
//"user_defined_7":null,
//"user_defined_8":null,
//"data":null,
//"expires_at":"2015-06-09T10:41:57.086Z",
//"created_at":"2015-06-09T10:26:57.255Z",
//"updated_at":"2015-06-09T10:26:57.255Z",
//"server_time":"2015-06-09T10:26:57.302Z"}
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Invoice {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String STATUS_BILLED = "billed";
    public static final String STATUS_UNPAID = "unpaid";
    public static final String STATUS_PAID = "paid";
    public static final String STATUS_READY_TO_SHIP = "ready_to_ship";
    public static final String STATUS_FULFILLED = "fulfilled";
    public static final String STATUS_UNDERPAID = "underpaid";
    public static final String STATUS_MERCHANT_REVIEW = "merchant_review";
    public static final String STATUS_REFUNDED = "refunded";
    public static final String STATUS_INVALID = "invalid";

    @JsonProperty("id")
    private String id;

    @JsonProperty("merchant_id")
    private String merchantId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("payment_address")
    private String paymentAddress;

    @JsonProperty("refund_address")
    private String refundAddress;

    @JsonProperty("refund_token")
    private String refundToken;

    @JsonProperty("crypto_url")
    private String cryptoUrl;

    @JsonProperty("gateway_url")
    private URL gatewayURL;

    @JsonProperty("redirect_url")
    private URL redirectUrl;

    @JsonProperty("callback_url")
    private URL callbackUrl;
    
    @JsonProperty("base_price")
    private String basePrice;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("crypto_balance_due")
    private String cryptoBalanceDue;

    @JsonProperty("crypto_payout_split")
    private Integer cryptoPayoutSplit;

    @JsonProperty("service_fee_rate")
    private Double serviceFeeRate;

    @JsonProperty("base_price_currency")
    private String basePriceCurrency;

    @JsonProperty("price_currency")
    private String priceCurrency;

    @JsonProperty("payout_currency")
    private String payoutCurrency;

    @JsonProperty("base_price_currency_detail")
    private CurrencyDetail basePriceCurrencyDetail;

    @JsonProperty("price_currency_detail")
    private CurrencyDetail priceCurrencyDetail;

    @JsonProperty("payout_currency_detail")
    private CurrencyDetail payoutCurrencyDetail;

    @JsonProperty("spot_rate")
    private Double spotRate;

    @JsonProperty("inverse_spot_rate")
    private Double spotRateInverse;

    @JsonProperty("usd_spot_rate")
    private Double spotRateUSD;

    @JsonProperty("valid_bill_payment_currencies")
    private String validBillPaymentCurrencies;

    @JsonProperty("valid_bill_payment_currencies_details")
    private CurrencyDetail validBillPaymentCurrenciesDetails;

    @JsonProperty("merchant_review_reason")
    private String merchantReviewReason;

    @JsonProperty("merchant_review_gain_loss")
    private String merchantReviewGainLoss;

    @JsonProperty("merchant_review_spot_rate")
    private String merchantReviewSpotRate;

    @JsonProperty("order_id")
    private String orderId;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Invoice.DATE_FORMAT, timezone = "UTC")
    private Date createdAt;

    @JsonProperty("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Invoice.DATE_FORMAT, timezone = "UTC")
    private Date updatedAt;

    @JsonProperty("expires_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Invoice.DATE_FORMAT, timezone = "UTC")
    private Date expiresAt;

    @JsonProperty("server_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Invoice.DATE_FORMAT, timezone = "UTC")
    private Date serverTs;
}
