package com.tenetmind.loans.loanapplication.domainmodel;

import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.repository.CurrencyRepository;
import com.tenetmind.loans.customer.domainmodel.Customer;
import com.tenetmind.loans.customer.repository.CustomerRepository;
import com.tenetmind.loans.loanapplication.repository.LoanApplicationRepository;
import org.junit.After;
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
    private CustomerRepository customerRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @After
    public void cleanUp() {
        loanApplicationRepository.deleteAll();
        currencyRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void shouldCreateLoanApplication() {
        //given
        Customer customer = new Customer("John", "Smith");
        Currency pln = new Currency("PLN");
        LoanApplication application = new LoanApplication(LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"), "New");

        //when
        customerRepository.save(customer);
        currencyRepository.save(pln);
        loanApplicationRepository.save(application);
        int applicationsSize = loanApplicationRepository.findAll().size();

        //then
        assertEquals(1, applicationsSize);
    }

    @Test
    public void shouldDeleteLoanApplicationAndNotDeleteCurrency() {
        //given
        Customer customer = new Customer("John", "Smith");
        Currency pln = new Currency("PLN");
        LoanApplication application = new LoanApplication(LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"), "New");
        customerRepository.save(customer);
        currencyRepository.save(pln);
        loanApplicationRepository.save(application);

        //when
        loanApplicationRepository.deleteAll();
        int currenciesSize = currencyRepository.findAll().size();

        //then
        assertEquals(1, currenciesSize);
    }

    @Test
    public void shouldDeleteLoanApplicationAndNotDeleteCustomer() {
        //given
        Customer customer = new Customer("John", "Smith");
        Currency pln = new Currency("PLN");
        LoanApplication application = new LoanApplication(LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"), "New");
        customerRepository.save(customer);
        currencyRepository.save(pln);
        loanApplicationRepository.save(application);

        //when
        loanApplicationRepository.deleteAll();
        int customersSize = customerRepository.findAll().size();

        //then
        assertEquals(1, customersSize);
    }

}
