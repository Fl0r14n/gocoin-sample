package com.gocoin.api.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URL;
import java.util.Date;
import lombok.Data;

/**
 * a simple POJO class to represent a merchant
 *
 * {
 * "id": "08d3bedf-7cb3-4ccc-9d20-cd221df9443d", "name": "BlingBling",
 * "address_1": "100 Main St.", "address_2": null, "city": "Venice", "region":
 * "CA", "country_code": "US", "postal_code": "90291", "contact_name": null,
 * "phone": "555-555-1234", "website": "http://merchant.com", "description":
 * null, "tax_id": null, "logo_url":null, "btc_payout_split":100,
 * "usd_payout_split", "created_at": "2013-08-13T04:07:43.867Z", "updated_at":
 * "2013-08-13T04:10:49.785Z" }
 */
@Data
public class Merchant {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("address_1")
    private String address1;
    @JsonProperty("address_2")
    private String address2;
    @JsonProperty("city")
    private String city;
    @JsonProperty("region")
    private String region;
    @JsonProperty("country_code")
    private String countryCode;
    @JsonProperty("postal_code")
    private String postalCode;
    @JsonProperty("contact_name")
    private String contactName;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("website")
    private URL website;
    @JsonProperty("description")
    private String description;
    @JsonProperty("tax_id")
    private String taxId;
    @JsonProperty("logo_url")
    private String logoUrl;
    @JsonProperty("btc_payout_split")
    private Double btcPayoutSplit;
    @JsonProperty("usd_payout_split")
    private Double usdPayoutSplit;
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("updated_at")
    private Date updatedAt;
}
