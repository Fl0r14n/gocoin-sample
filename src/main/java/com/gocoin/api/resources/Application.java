package com.gocoin.api.resources;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Calendar;
import lombok.Data;

/**
 * {
 * "id":63, "name":"AaronApp",
 * "uid":"661b989eda4e39e65456646f3a214e35039a8823666916dac717f746afa34018",
 * "secret":"977691490acff424973dfcf3fa32ba5161c7cda673af7b69a82c232e943f668b",
 * "redirect_uri":"http://www.google.com",
 * "created_at":"2013-12-05T12:23:12.776Z",
 * "updated_at":"2013-12-05T12:23:12.776Z",
 * "owner_id":"9127be09-24d0-4989-bc45-eb143c65506d", "owner_type":null,
 * "protected":false, "allow_grant_type_password":false,
 * "allow_grant_type_authorization_code":true,
 * "allow_grant_type_client_credentials":false,
 * "allow_grant_type_implicit":false }
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Application {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("uid")
    private String uid;
    @JsonProperty("secret")
    private String secret;
    @JsonProperty("redirect_uri")
    private String redirectUri;
    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Application.DATE_FORMAT, timezone = "UTC")
    private Calendar createdAt;
    @JsonProperty("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Application.DATE_FORMAT, timezone = "UTC")
    private Calendar updatedAt;
    @JsonProperty("owner_id")
    private String ownerId;
    @JsonProperty("owner_type")
    private String ownerType;
    @JsonProperty("protected")
    private boolean isProtected;
    @JsonProperty("allow_grant_type_password")
    private boolean allowGrantTypePassword;
    @JsonProperty("allow_grant_type_authorization_code")
    private boolean allowGrantTypeAuthCode;
    @JsonProperty("allow_grant_type_client_credentials")
    private boolean allowGrantTypeClientCredentials;
    @JsonProperty("allow_grant_type_implicit")
    private boolean allowGrantTypeImplicit;
}
