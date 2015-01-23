package com.gocoin.api.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * {
 * "grant_type" : "authorization_code", "code" :
 * "8240c24e6523d12eb6b3af0eceef9f9054f5f2bd2b849a9be419d756c919810b",
 * "client_id" :
 * "661b989eda4e39e65456646f3a214e35039a8823666916dac717f746afa34018",
 * "client_secret" :
 * "977691490acff424973dfcf3fa32ba5161c7cda673af7b69a82c232e943f668b",
 * "redirect_uri" : "http://www.google.com" }
 */
@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class AuthCode {

    @JsonProperty("grant_type")
    private String grantType = "authorization_code";
    @JsonProperty("code")
    private String code;
    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("client_secret")
    private String clientSecret;
    @JsonProperty("redirect_uri")
    private String redirectUri;

    public AuthCode(String code, String clientId, String clientSecret, String redirectUri) {
        this.code = code;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }
}
