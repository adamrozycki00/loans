package com.tenetmind.loans.currencyrate.service;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.repository.CurrencyRepository;
import com.tenetmind.loans.currency.service.CurrencyService;
import com.tenetmind.loans.currencyrate.domainmodel.CurrencyRate;
import com.tenetmind.loans.currencyrate.repository.CurrencyRateRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CurrencyRateServiceTest {

    @Autowired
    private CurrencyRateService service;

    @Autowired
    private CurrencyRateRepository repository;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Before
    public void setUp() {
        repository.deleteAll();
        currencyRepository.deleteAll();
    }

    @After
    public void cleanUp() {
        repository.deleteAll();
        currencyRepository.deleteAll();
    }

    @Test
    public void shouldSaveNewRate() throws CurrencyNotFoundException {
        //given
        String eur = "EUR";
        LocalDate date = LocalDate.of(2021, 2, 25);
        currencyService.save(new Currency(eur));

        //when
        service.getNewRateAndSave(eur, date);
        int currencyRatesSize = service.findAll().size();
        CurrencyRate rate = service.getRate(date, eur).get();

        //then
        assertEquals(1, currencyRatesSize);
        assertEquals(4.5143, rate.getRate().doubleValue(), .0001);
    }

    @Test
    public void shouldNotSaveRetrievedRateWhenTheRateExists() throws CurrencyNotFoundException {
        //given
        String eur = "EUR";
        LocalDate date = LocalDate.of(2021, 2, 25);
        Currency currency = currencyService.save(new Currency(eur));
        BigDecimal testingRate = BigDecimal.ONE;
        service.save(new CurrencyRate(date, currency, testingRate));

        //when
        service.getNewRateAndSave(eur, date);
        Optional<CurrencyRate> rateOptional = service.getRate(date, currency.getName());

        //then
        BigDecimal rate = rateOptional.get().getRate();
        int scale = rate.scale();
        assertEquals(testingRate.setScale(scale), rate);
    }

}