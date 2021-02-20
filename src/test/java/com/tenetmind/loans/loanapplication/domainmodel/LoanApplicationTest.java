package com.tenetmind.loans.loanapplication.domainmodel;

import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.service.CurrencyService;
import com.tenetmind.loans.customer.domainmodel.Customer;
import com.tenetmind.loans.customer.service.CustomerService;
import com.tenetmind.loans.loanapplication.service.LoanApplicationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoanApplicationTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private LoanApplicationService service;

    @Test
    public void shouldCreateLoanApplication() {
        //given
        Customer customer = new Customer(1L, "John", "Smith");
        Currency pln = new Currency(1L, "PLN");
        LoanApplication application = new LoanApplication(1L, LocalDateTime.now(), customer, pln,
                new BigDecimal(1000), 12, new BigDecimal(.05), "new");

        //when
        customerService.save(customer);
        currencyService.save(pln);
        service.save(application);
        int applicationsSize = service.findAll().size();

        //then
        assertEquals(1, applicationsSize);
    }

}
