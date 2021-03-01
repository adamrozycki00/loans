package com.tenetmind.loans.currencyrate.client;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currencyrate.domainmodel.CurrencyRate;

import java.time.LocalDate;
import java.util.Optional;

public interface CurrencyRateClient {

    Optional<CurrencyRate> getCurrencyRate(String currencyName, LocalDate date) throws CurrencyNotFoundException;

    default Optional<CurrencyRate> getCurrencyRate(String currencyName)
            throws CurrencyNotFoundException {
        return getCurrencyRate(currencyName, LocalDate.now());
    }


}
