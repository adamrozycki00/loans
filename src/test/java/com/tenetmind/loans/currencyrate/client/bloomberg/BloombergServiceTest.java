package com.tenetmind.loans.currencyrate.client.bloomberg;

import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.repository.CurrencyRepository;
import com.tenetmind.loans.currencyrate.repository.CurrencyRateRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BloombergServiceTest {

    @Autowired
    private BloombergService service;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CurrencyRateRepository currencyRateRepository;

    @Before
    public void setUp() {
        currencyRateRepository.deleteAll();
        currencyRepository.deleteAll();
    }

    @After
    public void cleanUp() {
        currencyRateRepository.deleteAll();
        currencyRepository.deleteAll();
    }

    @Test
    public void shouldCreateCurrencyRates() {
//        //given
//        currencyRepository.save(new Currency("EUR"));
//        currencyRepository.save(new Currency("USD"));
//        currencyRepository.save(new Currency("GBP"));
//        int sizeBefore = currencyRateRepository.findAll().size();
//
//        //when
//        service.getNewBloombergRateAndSaveOrUpdate();
//        int sizeAfter = currencyRateRepository.findAll().size();
//
//        //then
//        assertEquals(0, sizeBefore);
//        assertEquals(3, sizeAfter);
    }

}