package com.tenetmind.loans.interestrate.domainmodel;

import com.tenetmind.loans.currency.domainmodel.CurrencyDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class InterestRateDto {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final CurrencyDto currencyDto;
    private final BigDecimal rate;

    @Override
    public String toString() {
        return "InterestRateDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", currencyDto=" + currencyDto +
                ", rate=" + rate +
                '}';
    }

}

