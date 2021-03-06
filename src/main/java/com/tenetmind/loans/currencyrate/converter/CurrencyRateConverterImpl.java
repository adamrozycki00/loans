package com.tenetmind.loans.currencyrate.converter;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.service.CurrencyService;
import com.tenetmind.loans.currencyrate.controller.CurrencyRateNotFoundException;
import com.tenetmind.loans.currencyrate.domainmodel.CurrencyRate;
import com.tenetmind.loans.currencyrate.service.CurrencyRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static java.math.BigDecimal.ROUND_HALF_EVEN;

@Component
@Scope("prototype")
public abstract class CurrencyRateConverterImpl implements CurrencyRateConverter {

    protected String currencyRateName = "";

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private CurrencyRateService currencyRateService;

    @Override
    public BigDecimal convert(BigDecimal originalAmount, String originalCurrencyName,
                              String outputCurrencyName, LocalDate date)
            throws CurrencyNotFoundException, CurrencyRateNotFoundException {
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

    @Override
    public BigDecimal convertToPln(BigDecimal originalAmount, String originalCurrencyName, LocalDate date)
            throws CurrencyNotFoundException, CurrencyRateNotFoundException {

        if (originalCurrencyName.equals("PLN")) {
            return originalAmount;
        }

        Optional<CurrencyRate> currentRate = currencyRateService.getCurrentRate(originalCurrencyName, date);
        if (currentRate.isEmpty()) throw new CurrencyRateNotFoundException();

        BigDecimal rate = currentRate.get().getRate();
        return originalAmount.multiply(rate).setScale(4, ROUND_HALF_EVEN);
    }

    @Override
    public BigDecimal convertFromPln(BigDecimal originalAmount, String outputCurrencyName, LocalDate date)
            throws CurrencyNotFoundException, CurrencyRateNotFoundException {

        if (outputCurrencyName.equals("PLN")) {
            return originalAmount;
        }

        Optional<CurrencyRate> currentRate = currencyRateService.getCurrentRate(outputCurrencyName, date);
        if (currentRate.isEmpty()) throw new CurrencyRateNotFoundException();

        BigDecimal rate = currentRate.get().getRate();
        return originalAmount.divide(rate, 4, ROUND_HALF_EVEN);
    }

}
