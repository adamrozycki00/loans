package com.tenetmind.loans.currencyrate.client;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currencyrate.domainmodel.CurrencyRate;

import java.time.LocalDate;
import java.util.Optional;

public interface CurrencyRateClient {

    String getName();

    Optional<CurrencyRate> prepareCurrencyRate(String currencyName, LocalDate date)
            throws CurrencyNotFoundException;

    default Optional<CurrencyRate> prepareCurrencyRate(String currencyName)
            throws CurrencyNotFoundException {
        return prepareCurrencyRate(currencyName, LocalDate.now());
    }

}
