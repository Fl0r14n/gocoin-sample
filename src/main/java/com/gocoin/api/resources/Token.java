package com.gocoin.api.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * {
 * "access_token":"ae1b4f36a6eef7021318a0edd73daaa5fd0f32b07b14b0236956edcee61619ba",
 * "token_type":"bearer", "scope":"user_read" }
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Token {

    @JsonProperty("access_token")
    private final String token;
    @JsonProperty("token_type")
    private final String type = "bearer";
    @JsonProperty("scope")
    private final String scope;
}
