package com.tenetmind.loans.currencyrate.client.nbp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NbpMidRateDto {

    @JsonProperty("no")
    private String no;

    @JsonProperty("effectiveDate")
    private String effectiveDate;

    @JsonProperty("mid")
    private String mid;

    @Override
    public String toString() {
        return "{" +
                "no='" + no + '\'' +
                ", effectiveDate='" + effectiveDate + '\'' +
                ", mid='" + mid + '\'' +
                '}';
    }
}
