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
public class BloombergSingleRateDetailsDto {

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("sourceCurrency")
    private String sourceCurrency;

    @JsonProperty("last")
    private String rate;

    @Override
    public String toString() {
        return "{" +
                "currency='" + currency + '\'' +
                ", sourceCurrency='" + sourceCurrency + '\'' +
                ", rate='" + rate + '\'' +
                '}';
    }
}
