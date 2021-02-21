package com.tenetmind.loans.currency.service.converter;

import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.service.CurrencyService;
import com.tenetmind.loans.currency.service.converter.CurrencyConversionException;
import com.tenetmind.loans.currency.service.converter.CurrencyConverter;
import com.tenetmind.loans.currencyrate.domainmodel.CurrencyRate;
import com.tenetmind.loans.currencyrate.service.CurrencyRateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CurrencyConverterTest {

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private CurrencyRateService currencyRateService;

    @Autowired
    private CurrencyConverter converter;

    @Test
    public void shouldConvertGivenCurrencies() throws CurrencyConversionException {
        //given
        Currency pln = new Currency("pln");
        currencyService.save(pln);

        Currency eur = new Currency("eur");
        currencyService.save(eur);

        Currency usd = new Currency("usd");
        currencyService.save(usd);

        CurrencyRate eurRate = new CurrencyRate(LocalDate.now(), eur, new BigDecimal("4.0000"));
        currencyRateService.save(eurRate);

        CurrencyRate usdRate = new CurrencyRate(LocalDate.now(), usd, new BigDecimal("3.0000"));
        currencyRateService.save(usdRate);

        //when
        BigDecimal eurToUsd =
                converter.convert(new BigDecimal("1"), eur, usd, LocalDate.now());
        BigDecimal usdToEur =
                converter.convert(new BigDecimal("1"), usd, eur, LocalDate.now());

        //then
        assertEquals(new BigDecimal("1.3333"), eurToUsd);
        assertEquals(new BigDecimal(".7500"), usdToEur);
    }

}
