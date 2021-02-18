package com.tenetmind.loans.currency.domainmodel;

import com.tenetmind.loans.currencyrate.domainmodel.CurrencyRate;
import com.tenetmind.loans.interestrate.domainmodel.InterestRate;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class CurrencyDto {

    private final Long id;
    private final String name;
    private final List<CurrencyRate> rates;
    private final List<InterestRate> interestRates;

}
