package com.gocoin.api.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class SearchQueryBuilder {
    private static final String MERCHANT_ID = "merchant_id";
    private static final String STATUS = "status";
    private static final String DATE_START = "start_time";
    private static final String DATE_STOP = "end_time";
    private static final String ORDER_ID = "order_id";
    private static final String CUSTOMER_EMAIL = "customer_email";
    private static final String CUSTOMER_NAME = "customer_name";
    private static final String ITEM_NAME = "item_name";
    private static final String ITEM_SKU = "item_sku";
    private static final String DATE_UPDATED = "updated_since";
    private static final String PAGE = "page";
    private static final String PER_PAGE = "per_page";
    
    public static enum STATUS {
        UNPAID,
        UNDERPAID,
        PAID,
        READY_TO_SHIP;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
    
    public static final String TIMESTAMP_PATTERN_CUT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public final DateFormat df; 

    private Map<String, String> params;
    
    public SearchQueryBuilder() {
        df = new SimpleDateFormat(TIMESTAMP_PATTERN_CUT);
        params = new HashMap<>();
    }
    
    public SearchQueryBuilder(String merchantId) {
        this();
        this.merchantId(merchantId);
    }
    
    public SearchQueryBuilder(Map<String, String> queryParams) {
        this();
        for(Map.Entry<String, String> entry: queryParams.entrySet()) {
            switch(entry.getKey()) {
                case MERCHANT_ID: {
                    this.merchantId(entry.getValue());
                    break;
                }
                case STATUS: {
                    this.merchantId(entry.getValue());
                    break;
                }
                case DATE_START: {
                    this.startTime(entry.getValue());
                    break;
                }
                case DATE_STOP: {
                    this.stopTime(entry.getValue());
                    break;
                }
                case ORDER_ID: {
                    this.orderId(entry.getValue());
                    break;
                }
                case CUSTOMER_EMAIL: {
                    this.customerEmail(entry.getValue());
                    break;
                }
                case CUSTOMER_NAME: {
                    this.customerName(entry.getValue());
                    break;
                }
                case ITEM_NAME: {
                    this.itemName(entry.getValue());
                    break;
                }
                case ITEM_SKU: {
                    this.itemSKU(entry.getValue());
                    break;
                }
                case DATE_UPDATED: {
                    this.updatedSince(entry.getValue());
                    break;
                }
                case PAGE: {
                    this.pageNumber(Integer.parseInt(entry.getValue()));
                    break;
                }
                case PER_PAGE: {
                    this.itemsPerPage(Integer.parseInt(entry.getValue()));
                    break;
                }
            }
        }
    }
    
    public SearchQueryBuilder merchantId(String merchantId) {
        params.put(MERCHANT_ID, merchantId);
        return this;
    } 

    public SearchQueryBuilder status(STATUS status) {
        params.put(STATUS, status.toString());
        return this;
    }
    
    public SearchQueryBuilder startTime(Date start) {
        params.put(DATE_START, df.format(start));
        return this;
    }
    
    public SearchQueryBuilder startTime(String start) {
        params.put(DATE_START, start);
        return this;
    }
    
    public SearchQueryBuilder stopTime(Date end) {
        params.put(DATE_STOP, df.format(end));
        return this;
    }
    
    public SearchQueryBuilder stopTime(String end) {
        params.put(DATE_STOP, end);
        return this;
    }
    
    public SearchQueryBuilder orderId(String orderId) {
        params.put(ORDER_ID, orderId);
        return this;
    }
    
    public SearchQueryBuilder customerEmail(String customerEmail) {
        params.put(CUSTOMER_EMAIL, customerEmail);
        return this;
    }
    
    public SearchQueryBuilder customerName(String customerName) {
        params.put(CUSTOMER_NAME, customerName);
        return this;
    }

    public SearchQueryBuilder itemName(String itemName) {
        params.put(ITEM_NAME, itemName);
        return this;
    }
    
    public SearchQueryBuilder itemSKU(String itemSKU) {
        params.put(ITEM_SKU, itemSKU);
        return this;
    }
    
    public SearchQueryBuilder updatedSince(Date updatedSince) {
        params.put(DATE_UPDATED, df.format(updatedSince));
        return this;
    }
    
    public SearchQueryBuilder updatedSince(String updatedSince) {
        params.put(DATE_UPDATED, updatedSince);
        return this;
    }
    
    public SearchQueryBuilder pageNumber(Integer pageNumber) {
        params.put(PAGE, ""+pageNumber);
        return this;
    }
    
    public SearchQueryBuilder itemsPerPage(Integer itemsPerPage) {
        params.put(PER_PAGE, ""+itemsPerPage);
        return this;
    }

    public Map<String, String> build() {
        return params;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("?");
        for(Map.Entry<String, String> entry: params.entrySet()) {
            builder.append(entry.getKey()).append("=").append(entry.getValue()).append('&');
        }
        builder.deleteCharAt(builder.length()-1);
        try {
            return URLEncoder.encode(builder.toString(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return "SearchQueryBuilder:toString encoding error";
        }
    }
}
