package com.gocoin.api.resources;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URL;
import java.util.Date;
import lombok.Data;

/**
 * {
 * "id":"9127be09-24d0-4989-bc45-eb143c65506d",
 * "email":"aaronlabella@gmail.com", "first_name":"Aaron",
 * "last_name":"Labella", "created_at":"2013-12-05T05:57:19.548Z",
 * "updated_at":"2013-12-05T12:20:00.593Z", "image_url":null, "confirmed":true,
 * "merchant_id":"81d9b056-6351-4ca9-950c-2e7932db0aec" }
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    @JsonProperty("id")
    private String id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("email")
    private String email;
    @JsonProperty("created_at")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=User.DATE_FORMAT, timezone="UTC")
    private Date createdAt;
    @JsonProperty("updated_at")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=User.DATE_FORMAT, timezone="UTC")
    private Date updatedAt;
    @JsonProperty("image_url")
    private URL imageURL;
    @JsonProperty("confirmed")
    private boolean confirmed;
    @JsonProperty("merchant_id")
    private String merchantId;
}
