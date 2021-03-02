package com.tenetmind.loans.currencyrate.client.bloomberg;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.service.CurrencyService;
import com.tenetmind.loans.currencyrate.client.CurrencyRateClient;
import com.tenetmind.loans.currencyrate.client.config.CurrencyRateClientConfiguration;
import com.tenetmind.loans.currencyrate.domainmodel.CurrencyRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@Qualifier("bloombergClient")
public class BloombergClient implements CurrencyRateClient {

    @Autowired
    private CurrencyRateClientConfiguration config;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CurrencyService currencyService;

    @Override
    public Optional<CurrencyRate> prepareCurrencyRate(String currencyName, LocalDate date)
            throws CurrencyNotFoundException {
        Optional<Currency> currency = currencyService.find(currencyName);

        if (currency.isEmpty())
            throw new CurrencyNotFoundException();

        Optional<BloombergRatesDto> bloombergRates = getBloombergRates(currencyName);

        if (bloombergRates.isPresent()) {
            BigDecimal rate = null;
            switch (currencyName) {
                case "EUR":
                    rate = new BigDecimal(bloombergRates.get().getResult().getEurPlnRate().getRate());
                    break;
                case "USD":
                    rate = new BigDecimal(bloombergRates.get().getResult().getUsdPlnRate().getRate());
                    break;
                case "GBP":
                    rate = new BigDecimal(bloombergRates.get().getResult().getGbpPlnRate().getRate());
                    break;
            }
            return Optional.of(new CurrencyRate(getName(), LocalDate.now(), new Currency(currencyName), rate));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public String getName() {
        return "Bloomberg";
    }

    private Optional<BloombergRatesDto> getBloombergRates(String... codes) {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(APPLICATION_JSON);
        headers.setAccept(singletonList(APPLICATION_JSON));

        headers.set("x-rapidapi-key", config.getRapidApiKey());
        headers.set("x-rapidapi-host", config.getRapidApiHost());

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
    }

    private URI getBloombergUrl(String... codes) {
        String codeList = String.join(",", codes);
        return UriComponentsBuilder.fromHttpUrl(config.getBloombergApiEndpoint())
                .queryParam("id", "pln," + codeList)
                .build().encode().toUri();
    }

}
