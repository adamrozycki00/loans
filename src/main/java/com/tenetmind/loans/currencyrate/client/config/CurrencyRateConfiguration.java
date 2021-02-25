package com.tenetmind.loans.currencyrate.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CurrencyRateConfiguration {

    @Value("http://api.nbp.pl/api/exchangerates/rates/A/")
    private String nbpApiEndpoint;

    public String getNbpApiEndpoint() {
        return nbpApiEndpoint;
    }

}
