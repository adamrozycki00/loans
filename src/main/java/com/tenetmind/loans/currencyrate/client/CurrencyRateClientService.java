package com.tenetmind.loans.currencyrate.client;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.service.CurrencyService;
import com.tenetmind.loans.currencyrate.client.config.CurrencyRateClientConfiguration;
import com.tenetmind.loans.currencyrate.domainmodel.CurrencyRate;
import com.tenetmind.loans.currencyrate.repository.CurrencyRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

public abstract class CurrencyRateClientService {

    @Autowired
    private CurrencyRateClientConfiguration config;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private CurrencyRateRepository repository;

    public abstract CurrencyRateClient getClient();

    public abstract String getName();

    public void getAndSave(String currencyName, LocalDate date) throws CurrencyNotFoundException {
        CurrencyRateClient client = getClient();
        Optional<CurrencyRate> optionalCurrencyRate = getClient().prepareCurrencyRate(currencyName, date);

        if (optionalCurrencyRate.isPresent()) {
            CurrencyRate rateOnSave = optionalCurrencyRate.get();

            Currency currency = currencyService.find(rateOnSave.getCurrency().getName())
                    .orElseThrow(CurrencyNotFoundException::new);

            Optional<CurrencyRate> retrievedCurrencyRate =
                    repository.findByNameAndDateAndCurrency(getName(), LocalDate.now(), currency);

            if (retrievedCurrencyRate.isEmpty()) {
                rateOnSave.setCurrency(currency);
                repository.save(rateOnSave);
            }
        }
    }

    public void getAndSave(String currencyName) throws CurrencyNotFoundException {
        getAndSave(currencyName, LocalDate.now());
    }

    public CurrencyRateClientConfiguration getConfig() {
        return config;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

}
