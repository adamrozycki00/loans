package com.tenetmind.loans.currencyrate.converter;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.repository.CurrencyRepository;
import com.tenetmind.loans.currency.service.CurrencyService;
import com.tenetmind.loans.currencyrate.controller.CurrencyRateNotFoundException;
import com.tenetmind.loans.currencyrate.domainmodel.CurrencyRate;
import com.tenetmind.loans.currencyrate.repository.CurrencyRateRepository;
import com.tenetmind.loans.currencyrate.service.CurrencyRateService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NbpRateConverterTest {

    private CurrencyRateConverter converter;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CurrencyRateService currencyRateService;

    @Autowired
    private CurrencyRateRepository currencyRateRepository;

    public CurrencyRateConverter getConverter() {
        return converter;
    }

    @Qualifier("nbpRateConverter")
    @Autowired
    public void setConverter(CurrencyRateConverter converter) {
        this.converter = converter;
    }

    @Before
    public void setUp() {
        currencyRateRepository.deleteAll();
        currencyRepository.deleteAll();
        currencyService.populateWithMainCurrencies();
    }

    @After
    public void cleanUp() {
        currencyRateRepository.deleteAll();
        currencyRepository.deleteAll();
    }

    @Test
    public void shouldConvertGivenCurrencies() throws CurrencyNotFoundException, CurrencyRateNotFoundException {
        //given
        Currency eur = currencyService.find("eur").get();
        Currency usd = currencyService.find("usd").get();

        CurrencyRate eurRate = new CurrencyRate("NBP", LocalDate.now().minusDays(1), eur, new BigDecimal("4.0000"));
        currencyRateService.save(eurRate);

        CurrencyRate usdRate = new CurrencyRate("NBP", LocalDate.now().minusDays(1), usd, new BigDecimal("3.0000"));
        currencyRateService.save(usdRate);

        //when
        BigDecimal eurToUsd =
                converter.convert(new BigDecimal("1"), "eur", "usd", LocalDate.now());
        BigDecimal usdToEur =
                converter.convert(new BigDecimal("1"), "usd", "eur", LocalDate.now());

        //then
        assertEquals(new BigDecimal("1.3333"), eurToUsd);
        assertEquals(new BigDecimal(".7500"), usdToEur);
    }

}