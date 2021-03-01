package com.tenetmind.loans.currencyrate.client;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currencyrate.client.config.CurrencyRateClientConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

public abstract class CurrencyRateClientService {

    @Autowired
    private CurrencyRateClientConfiguration config;

    @Autowired
    private RestTemplate restTemplate;

    protected CurrencyRateClient client;

    protected abstract void setClient(CurrencyRateClient client);

    public abstract void getAndSave(String currencyName, LocalDate date) throws CurrencyNotFoundException;

    public CurrencyRateClient getClient() {
        return client;
    }

    public CurrencyRateClientConfiguration getConfig() {
        return config;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

}
