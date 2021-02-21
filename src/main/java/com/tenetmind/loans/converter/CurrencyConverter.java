package com.tenetmind.loans.converter;

import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currencyrate.domainmodel.CurrencyRate;
import com.tenetmind.loans.currencyrate.service.CurrencyRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static java.math.BigDecimal.ROUND_HALF_EVEN;
import static java.util.Optional.empty;

@Component
public class CurrencyConverter {

    @Autowired
    private CurrencyRateService service;

    public Optional<BigDecimal> convert(BigDecimal originalAmount, Currency originalCurrency,
                                        Currency outputCurrency, LocalDate date) {
        if (originalCurrency.equals(new Currency("PLN"))) {
            return convertFromPln(originalAmount, outputCurrency, date);
        }

        if (outputCurrency.equals(new Currency("PLN"))) {
            return convertToPln(originalAmount, originalCurrency, date);
        }

        Optional<BigDecimal> amountInPln =
                convertToPln(originalAmount, originalCurrency, date);

        if (amountInPln.isPresent()) {
            return convertFromPln(amountInPln.get(), outputCurrency, date);
        } else {
            return empty();
        }
    }

    public Optional<BigDecimal> convertToPln(BigDecimal originalAmount, Currency originalCurrency, LocalDate date) {
        Optional<CurrencyRate> originalRate = service.getRate(date, originalCurrency);
        return originalRate
                .map(CurrencyRate::getRate)
                .map(originalAmount::multiply);
    }

    public Optional<BigDecimal> convertFromPln(BigDecimal originalAmount, Currency outputCurrency, LocalDate date) {
        Optional<CurrencyRate> outputRate = service.getRate(date, outputCurrency);
        return outputRate
                .map(CurrencyRate::getRate)
                .map(rate -> originalAmount.divide(rate, 4, ROUND_HALF_EVEN));
    }

}
