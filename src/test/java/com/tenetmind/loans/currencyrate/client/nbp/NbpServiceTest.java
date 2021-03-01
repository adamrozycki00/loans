package com.tenetmind.loans.currencyrate.client.nbp;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
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

import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NbpServiceTest {

    @Autowired
    private NbpService service;

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
    public void shouldCreateCurrencyRate() throws CurrencyNotFoundException {
        //given
        Currency gbp = new Currency("GBP");
        currencyRepository.save(gbp);
        int sizeBefore = currencyRateRepository.findAll().size();

        //when
        service.getNbpRateAndSave("GBP", LocalDate.of(2021, 2, 25));
        int sizeAfter = currencyRateRepository.findAll().size();

        //then
        assertEquals(0, sizeBefore);
        assertEquals(1, sizeAfter);
    }

}