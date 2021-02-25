package com.tenetmind.loans.currencyrate.converter;

import org.springframework.stereotype.Component;

@Component
public class BloombergRateConverter extends CurrencyRateConverterImpl {

    {
        setCurrencyRateName("Bloomberg");
    }

}
