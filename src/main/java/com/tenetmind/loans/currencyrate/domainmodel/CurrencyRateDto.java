package com.tenetmind.loans.currencyrate.domainmodel;

import com.tenetmind.loans.currency.domainmodel.CurrencyDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class CurrencyRateDto {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final CurrencyDto currencyDto;
    private final BigDecimal rate;

    @Override
    public String toString() {
        return "CurrencyRateDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", currencyDto=" + currencyDto +
                ", rate=" + rate +
                '}';
    }

}
