package com.tenetmind.loans.currencyrate.client.nbp;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.service.CurrencyService;
import com.tenetmind.loans.currencyrate.client.CurrencyRateClient;
import com.tenetmind.loans.currencyrate.client.config.CurrencyRateClientConfiguration;
import com.tenetmind.loans.currencyrate.domainmodel.CurrencyRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

@Component
@Qualifier("nbpClient")
public class NbpClient implements CurrencyRateClient {

    private static final String NAME = "Bloomberg";

    @Autowired
    private CurrencyRateClientConfiguration config;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CurrencyService currencyService;

    @Override
    public Optional<CurrencyRate> getCurrencyRate(String currencyName, LocalDate date)
            throws CurrencyNotFoundException {
        Optional<Currency> currency = currencyService.find(currencyName);

        if (currency.isEmpty())
            throw new CurrencyNotFoundException();

        Optional<NbpRatesDto> nbpRate = getNbpRates(currencyName, date.toString());

        if (nbpRate.isPresent()) {
            BigDecimal rate = new BigDecimal(nbpRate.get().getRates().get(0).getMid());
            CurrencyRate currencyRate = new CurrencyRate(NAME, date, currency.get(), rate);
            return Optional.of(currencyRate);
        } else {
            return Optional.empty();
        }
    }

    private Optional<NbpRatesDto> getNbpRates(String code, String date) {
        try {
            NbpRatesDto currencyRatesResponse = restTemplate.getForObject(getNbpUrl(code, date), NbpRatesDto.class);
            return ofNullable(currencyRatesResponse);
        } catch (Exception e) {
            return empty();
        }
    }

    private URI getNbpUrl(String code, String date) {
        return UriComponentsBuilder.fromHttpUrl(config.getNbpApiEndpoint() + code + '/' + date)
                .queryParam("format", "json")
                .build().encode().toUri();
    }

}
