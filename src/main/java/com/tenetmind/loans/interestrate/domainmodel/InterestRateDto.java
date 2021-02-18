package com.tenetmind.loans.interestrate.domainmodel;

import com.tenetmind.loans.currency.domainmodel.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class InterestRateDto {

    private Long id;
    private String name;
    private LocalDate date;
    private Currency currency;
    private BigDecimal rate;

}
