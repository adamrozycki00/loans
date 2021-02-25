package com.tenetmind.loans.currencyrate.converter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class NbpRateConverter extends CurrencyRateConverterImpl {

    {
        setCurrencyRateName("NBP");
    }

}
