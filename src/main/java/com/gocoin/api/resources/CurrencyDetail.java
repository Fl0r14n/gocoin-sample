package com.gocoin.api.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class CurrencyDetail {
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("symbol")
    private Character symbol;
    
    @JsonProperty("is_crypto")
    private Boolean isCrypto;
    
    @JsonProperty("sort_order")
    private Integer sortOrder;
    
    @JsonProperty("precision")
    private Integer precision;
    
    @JsonProperty("aliases")
    private String[] aliases;
}
