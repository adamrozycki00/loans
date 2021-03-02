package com.tenetmind.loans.currencyrate.service;

import com.tenetmind.loans.currency.repository.CurrencyRepository;
import com.tenetmind.loans.currency.service.CurrencyService;
import com.tenetmind.loans.currencyrate.repository.CurrencyRateRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

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
    public void shouldAddTodaysCurrencyRates() {
        //given
        repository.deleteAll();
        currencyRepository.deleteAll();
        currencyService.populateWithMainCurrencies();

        //when
        service.addTodaysRates();
        int size = repository.findByDate(LocalDate.now()).size();

        //then
        assertTrue(size > 0);
    }

}