package com.tenetmind.loans.currency.domainmodel;

import com.tenetmind.loans.currency.repository.CurrencyRepository;
import com.tenetmind.loans.currency.service.CurrencyService;
import org.junit.After;
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

}
