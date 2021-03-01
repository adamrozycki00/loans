package com.tenetmind.loans.currencyrate.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CurrencyRateClientConfiguration {

    @Value("http://api.nbp.pl/api/exchangerates/rates/A/")
    private String nbpApiEndpoint;

    @Value("https://bloomberg-market-and-financial-news.p.rapidapi.com/market/get-cross-currencies/")
    private String bloombergApiEndpoint;

    @Value("3950354d4emshb477cc6b8ca8423p1efbd6jsn64276a9900d6")
    private String rapidApiKey;

    @Value("bloomberg-market-and-financial-news.p.rapidapi.com")
    private String rapidApiHost;

    public String getNbpApiEndpoint() {
        return nbpApiEndpoint;
    }

    public String getBloombergApiEndpoint() {
        return bloombergApiEndpoint;
    }

    public String getRapidApiKey() {
        return rapidApiKey;
    }

    public String getRapidApiHost() {
        return rapidApiHost;
    }

}
