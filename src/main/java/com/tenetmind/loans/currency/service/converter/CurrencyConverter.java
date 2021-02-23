package com.tenetmind.loans.currency.service.converter;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.service.CurrencyService;
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
    private CurrencyRateService currencyRateService;

    @Autowired
    private CurrencyService currencyService;

    public BigDecimal convert(BigDecimal originalAmount, String originalCurrencyName,
                              String outputCurrencyName, LocalDate date)
            throws CurrencyConversionException, CurrencyNotFoundException {
        Currency originalCurrency = currencyService.find(originalCurrencyName)
                .orElseThrow(CurrencyNotFoundException::new);
        Currency outputCurrency = currencyService.find(outputCurrencyName)
                .orElseThrow(CurrencyNotFoundException::new);

        if (originalCurrency.equals(outputCurrency)) {
            return originalAmount;
        }

        if (originalCurrency.equals(new Currency("PLN"))) {
            return convertFromPln(originalAmount, outputCurrencyName, date);
        }

        if (outputCurrency.equals(new Currency("PLN"))) {
            return convertToPln(originalAmount, originalCurrencyName, date);
        }

        BigDecimal amountInPln =
                convertToPln(originalAmount, originalCurrencyName, date);

        return convertFromPln(amountInPln, outputCurrencyName, date);
    }

    public BigDecimal convertToPln(BigDecimal originalAmount, String originalCurrencyName, LocalDate date)
            throws CurrencyConversionException, CurrencyNotFoundException {
        Currency originalCurrency = currencyService.find(originalCurrencyName)
                .orElseThrow(CurrencyNotFoundException::new);

        if (originalCurrency.equals(new Currency("PLN"))) {
            return originalAmount;
        }

        Optional<CurrencyRate> originalRate = currencyRateService.getRate(date, originalCurrency);
        return originalRate
                .map(CurrencyRate::getRate)
                .map(originalAmount::multiply)
                .orElseThrow(CurrencyConversionException::new);
    }

    public BigDecimal convertFromPln(BigDecimal originalAmount, String outputCurrencyName, LocalDate date)
            throws CurrencyConversionException, CurrencyNotFoundException {
        Currency outputCurrency = currencyService.find(outputCurrencyName)
                .orElseThrow(CurrencyNotFoundException::new);

        if (outputCurrency.equals(new Currency("PLN"))) {
            return originalAmount;
        }

        Optional<CurrencyRate> outputRate = currencyRateService.getRate(date, outputCurrency);
        return outputRate
                .map(CurrencyRate::getRate)
                .map(rate -> originalAmount.divide(rate, 4, ROUND_HALF_EVEN))
                .orElseThrow(CurrencyConversionException::new);
    }

}
