package com.tenetmind.loans.currencyrate.client;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.service.CurrencyService;
import com.tenetmind.loans.currencyrate.domainmodel.CurrencyRate;
import com.tenetmind.loans.currencyrate.service.CurrencyRateService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Optional;

public abstract class CurrencyRateClientService {

    @Autowired
    private CurrencyRateService service;

    @Autowired
    private CurrencyService currencyService;

    public abstract CurrencyRateClient getClient();

    public abstract String getName();

    public void getAndSave(String currencyName, LocalDate date) throws CurrencyNotFoundException {
        Optional<CurrencyRate> optionalCurrencyRate = getClient().prepareCurrencyRate(currencyName, date);

        if (optionalCurrencyRate.isPresent()) {
            CurrencyRate rateOnSave = optionalCurrencyRate.get();

            Currency currency = currencyService.find(rateOnSave.getCurrency().getName())
                    .orElseThrow(CurrencyNotFoundException::new);

            Optional<CurrencyRate> retrievedCurrencyRate =
                    service.find(getName(), LocalDate.now(), currency.getName());

            if (retrievedCurrencyRate.isEmpty()) {
                rateOnSave.setCurrency(currency);
                service.save(rateOnSave);
            }
        }
    }

    public void getAndSave(String currencyName) throws CurrencyNotFoundException {
        getAndSave(currencyName, LocalDate.now());
    }

}
