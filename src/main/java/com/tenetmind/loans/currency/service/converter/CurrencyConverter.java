package com.tenetmind.loans.currency.service.converter;

import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currencyrate.domainmodel.CurrencyRate;
import com.tenetmind.loans.currencyrate.service.CurrencyRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static java.math.BigDecimal.ROUND_HALF_EVEN;

@Service
public class CurrencyConverter {

    @Autowired
    private CurrencyRateService service;

    public BigDecimal convert(BigDecimal originalAmount, Currency originalCurrency,
                              Currency outputCurrency, LocalDate date)
            throws CurrencyConversionException {
        if (originalCurrency.equals(new Currency("PLN"))) {
            return convertFromPln(originalAmount, outputCurrency, date);
        }

        if (outputCurrency.equals(new Currency("PLN"))) {
            return convertToPln(originalAmount, originalCurrency, date);
        }

        BigDecimal amountInPln =
                convertToPln(originalAmount, originalCurrency, date);

        return convertFromPln(amountInPln, outputCurrency, date);
    }

    public BigDecimal convertToPln(BigDecimal originalAmount, Currency originalCurrency, LocalDate date)
            throws CurrencyConversionException {
        Optional<CurrencyRate> originalRate = service.getRate(date, originalCurrency);
        return originalRate
                .map(CurrencyRate::getRate)
                .map(originalAmount::multiply)
                .orElseThrow(CurrencyConversionException::new);
    }

    public BigDecimal convertFromPln(BigDecimal originalAmount, Currency outputCurrency, LocalDate date)
            throws CurrencyConversionException {
        Optional<CurrencyRate> outputRate = service.getRate(date, outputCurrency);
        return outputRate
                .map(CurrencyRate::getRate)
                .map(rate -> originalAmount.divide(rate, 4, ROUND_HALF_EVEN))
                .orElseThrow(CurrencyConversionException::new);
    }

}
