package com.tenetmind.loans.interestrate.domainmodel;

import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.repository.CurrencyRepository;
import com.tenetmind.loans.interestrate.repository.InterestRateRepository;
import com.tenetmind.loans.interestrate.service.InterestRateService;
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
public class InterestRateTest {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private InterestRateRepository repository;

    @Autowired
    private InterestRateService service;

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

        InterestRate interestRate =
                new InterestRate("WIBOR_1M", LocalDate.now(), pln, new BigDecimal(".05"));

        //when
        service.save(interestRate);
        int interestRatesSize = repository.findAll().size();

        //then
        assertEquals(1, interestRatesSize);
    }

    @Test
    public void shouldDeleteInterestRateAndNotDeleteCurrency() {
        //given
        Currency pln = new Currency("PLN");
        currencyRepository.save(pln);

        InterestRate interestRate =
                new InterestRate("WIBOR_1M", LocalDate.now(), pln, new BigDecimal(".05"));
        repository.save(interestRate);

        //when
        service.deleteById(interestRate.getId());
        int interestRatesSize = repository.findAll().size();
        int currenciesSize = currencyRepository.findAll().size();

        //then
        assertEquals(0, interestRatesSize);
        assertEquals(1, currenciesSize);
    }

}
