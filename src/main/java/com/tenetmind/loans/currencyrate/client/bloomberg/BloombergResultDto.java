package com.tenetmind.loans.currencyrate.client.bloomberg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BloombergResultDto {

    @JsonProperty("EURPLN:cur")
    private BloombergSingleRateDto eurPlnRate;

    @JsonProperty("USDPLN:cur")
    private BloombergSingleRateDto usdPlnRate;

    @JsonProperty("GBPPLN:cur")
    private BloombergSingleRateDto gbpPlnRate;

    @Override
    public String toString() {
        return "{" +
                "eurPlnRate=" + eurPlnRate +
                ", usdPlnRate=" + usdPlnRate +
                ", gbpPlnRate=" + gbpPlnRate +
                '}';
    }

}
