package com.tenetmind.loans.currencyrate.domainmodel;

import com.tenetmind.loans.currency.domainmodel.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class CurrencyRateDto {

    private final Long id;
    private final LocalDate date;
    private final Currency currency;
    private final BigDecimal rate;

}
