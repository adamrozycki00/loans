package com.tenetmind.loans.currencyrate.converter;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CurrencyRateConverter {

    BigDecimal convert(BigDecimal originalAmount, String originalCurrencyName, String outputCurrencyName,
                       LocalDate date) throws CurrencyRateConversionException, CurrencyNotFoundException;

    BigDecimal convertToPln(BigDecimal originalAmount, String originalCurrencyName, LocalDate date)
            throws CurrencyRateConversionException, CurrencyNotFoundException;

    BigDecimal convertFromPln(BigDecimal originalAmount, String outputCurrencyName, LocalDate date)
            throws CurrencyRateConversionException, CurrencyNotFoundException;

}
