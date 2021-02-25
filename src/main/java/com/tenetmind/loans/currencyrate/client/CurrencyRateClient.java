package com.tenetmind.loans.currencyrate.client;

import com.tenetmind.loans.currencyrate.client.nbp.NbpRatesDto;
import com.tenetmind.loans.currencyrate.config.CurrencyRateConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

@Component
public class CurrencyRateClient {

    @Autowired
    private CurrencyRateConfiguration config;

    @Autowired
    private RestTemplate restTemplate;

    public Optional<NbpRatesDto> getNbpRates(String code, String date) {

            NbpRatesDto currencyRatesResponse = restTemplate.getForObject(getUrl(code, date), NbpRatesDto.class);
            return ofNullable(currencyRatesResponse);
    }

    private URI getUrl(String code, String date) {
        return UriComponentsBuilder.fromHttpUrl(config.getNbpApiEndpoint() + code + '/' + date)
                .queryParam("format", "json")
                .build().encode().toUri();
    }

}
