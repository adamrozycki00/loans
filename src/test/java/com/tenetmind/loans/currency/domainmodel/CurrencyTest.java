package com.tenetmind.loans.currency.domainmodel;

import com.tenetmind.loans.currency.repository.CurrencyRepository;
import com.tenetmind.loans.currency.service.CurrencyService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CurrencyTest {

    @Autowired
    private CurrencyRepository repository;

    @Autowired
    private CurrencyService service;

    @Before
    public void setUp() {
        repository.deleteAll();
    }

    @After
    public void cleanUp() {
        repository.deleteAll();
    }

    @Test
    public void shouldCreateCurrency() {
        //given
        Currency pln = new Currency("PLN");

        //when
        service.save(pln);
        int currenciesSize = repository.findAll().size();

        //then
        assertEquals(1, currenciesSize);
    }

    @Test
    public void shouldUpdateCurrency() {
        //given
        Currency pln = new Currency("PLN");
        service.save(pln);
        long plnId = pln.getId();

        Currency usd = new Currency(plnId, "USD");

        //when
        service.save(usd);
        int currenciesSizeAfterSavingUsd = repository.findAll().size();
        String updatedPlnName = repository.findById(plnId).get().getName();

        //then
        assertEquals(1, currenciesSizeAfterSavingUsd);
        assertEquals("USD", updatedPlnName);
    }

}
