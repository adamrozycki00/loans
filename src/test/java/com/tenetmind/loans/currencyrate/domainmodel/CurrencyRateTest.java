package com.tenetmind.loans.currencyrate.domainmodel;

import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.repository.CurrencyRepository;
import com.tenetmind.loans.currencyrate.repository.CurrencyRateRepository;
import com.tenetmind.loans.currencyrate.service.CurrencyRateService;
import org.junit.After;
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
public class CurrencyRateTest {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CurrencyRateRepository repository;

    @Autowired
    private CurrencyRateService service;

    @After
    public void cleanUp() {
        repository.deleteAll();
        currencyRepository.deleteAll();
    }

    @Test
    public void shouldCreateCurrencyRate() {
        //given
        Currency pln = new Currency("PLN");
        currencyRepository.save(pln);

        CurrencyRate currencyRate = new CurrencyRate(LocalDate.now(), pln, new BigDecimal(".05"));

        //when
        service.save(currencyRate);
        int currencyRatesSize = repository.findAll().size();

        //then
        assertEquals(1, currencyRatesSize);
    }

    @Test
    public void shouldDeleteCurrencyRateAndNotDeleteCurrency() {
        //given
        Currency pln = new Currency("PLN");
        currencyRepository.save(pln);

        CurrencyRate currencyRate = new CurrencyRate(LocalDate.now(), pln, new BigDecimal(".05"));
        repository.save(currencyRate);

        //when
        service.deleteById(currencyRate.getId());
        int currencyRatesSize = repository.findAll().size();
        int currenciesSize = currencyRepository.findAll().size();

        //then
        assertEquals(0, currencyRatesSize);
        assertEquals(1, currenciesSize);
    }

}
