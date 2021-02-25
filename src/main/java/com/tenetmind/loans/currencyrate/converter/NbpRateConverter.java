package com.tenetmind.loans.currencyrate.converter;

import org.springframework.stereotype.Component;

@Component
public class NbpRateConverter extends CurrencyRateConverterImpl {

    {
        setCurrencyRateName("NBP");
    }

}
