package com.tenetmind.loans.currencyrate.client;

import com.tenetmind.loans.currencyrate.client.bloomberg.BloombergRatesDto;
import com.tenetmind.loans.currencyrate.client.nbp.NbpRatesDto;
import com.tenetmind.loans.currencyrate.client.config.CurrencyRateConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class CurrencyRateClient {

    @Autowired
    private CurrencyRateConfiguration config;

    @Autowired
    private RestTemplate restTemplate;


    public Optional<NbpRatesDto> getNbpRates(String code, String date) {
        try {
            NbpRatesDto currencyRatesResponse = restTemplate.getForObject(getNbpUrl(code, date), NbpRatesDto.class);
            return ofNullable(currencyRatesResponse);
        } catch (Exception e) {
            return empty();
        }
    }

    public Optional<BloombergRatesDto> getBloombergRates(String... codes) {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(APPLICATION_JSON);
        headers.setAccept(singletonList(APPLICATION_JSON));

        headers.set("x-rapidapi-key", config.getRapidapiKey());
        headers.set("x-rapidapi-host", config.getRapidapiHost());

        HttpEntity<BloombergRatesDto> request = new HttpEntity<>(headers);

        ResponseEntity<BloombergRatesDto> response = restTemplate.exchange(
                getBloombergUrl(codes).toString(),
                GET,
                request,
                BloombergRatesDto.class,
                1
        );

        try {
            BloombergRatesDto currencyRatesResponse = response.getBody();
            return ofNullable(currencyRatesResponse);
        } catch (Exception e) {
            return empty();
        }
//        return ofNullable(currencyRatesResponse);

//        try {
//            BloombergRatesDto currencyRatesResponse = response.getBody();
//            return ofNullable(currencyRatesResponse);
//        } catch (Exception e) {
//            return empty();
//        }

//        // check response
//        if (response.getStatusCode() == HttpStatus.OK) {
//            System.out.println("Request Successful.");
//            System.out.println(response.getBody());
//        } else {
//            System.out.println("Request Failed");
//            System.out.println(response.getStatusCode());
//        }



    }

    private URI getNbpUrl(String code, String date) {
        return UriComponentsBuilder.fromHttpUrl(config.getNbpApiEndpoint() + code + '/' + date)
                .queryParam("format", "json")
                .build().encode().toUri();
    }

    private URI getBloombergUrl(String... codes) {
        String codeList = String.join(",", codes);
        return UriComponentsBuilder.fromHttpUrl(config.getBloombergApiEndpoint())
                .queryParam("id", "pln," + codeList)
                .build().encode().toUri();
    }

}
