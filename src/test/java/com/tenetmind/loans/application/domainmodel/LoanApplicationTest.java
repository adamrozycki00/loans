package com.tenetmind.loans.application.domainmodel;

import com.tenetmind.loans.application.controller.LoanApplicationNotFoundException;
import com.tenetmind.loans.application.service.InvalidApplicationStatusException;
import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.repository.CurrencyRepository;
import com.tenetmind.loans.customer.domainmodel.Customer;
import com.tenetmind.loans.customer.repository.CustomerRepository;
import com.tenetmind.loans.application.repository.LoanApplicationRepository;
import com.tenetmind.loans.application.service.LoanApplicationService;
import com.tenetmind.loans.loan.repository.LoanRepository;
import com.tenetmind.loans.loan.service.InvalidLoanStatusException;
import org.junit.After;
import org.junit.Before;
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
    private LoanApplicationRepository repository;

    @Autowired
    private LoanApplicationService service;

    @Autowired
    private LoanRepository loanRepository;

    @Before
    public void setUp() {
        loanRepository.deleteAll();
        repository.deleteAll();
        currencyRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @After
    public void cleanUp() {
        loanRepository.deleteAll();
        repository.deleteAll();
        currencyRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void shouldCreateLoanApplication() throws InvalidApplicationStatusException, InvalidLoanStatusException,
            LoanApplicationNotFoundException, CurrencyNotFoundException {
        //given
        Customer customer = new Customer("John", "Smith", "12345");
        customerRepository.save(customer);

        Currency pln = new Currency("PLN");
        currencyRepository.save(pln);

        LoanApplication application = new LoanApplication(LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"));

        //when
        service.save(application);
        int applicationsSize = repository.findAll().size();

        //then
        assertEquals(1, applicationsSize);
    }

    @Test
    public void shouldDeleteLoanApplicationAndNotDeleteCurrency() {
        //given
        Customer customer = new Customer("John", "Smith", "12345");
        customerRepository.save(customer);

        Currency pln = new Currency("PLN");
        currencyRepository.save(pln);

        LoanApplication application = new LoanApplication(LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"));
        repository.save(application);

        //when
        service.deleteById(application.getId());
        int currenciesSize = currencyRepository.findAll().size();

        //then
        assertEquals(1, currenciesSize);
    }

    @Test
    public void shouldDeleteLoanApplicationAndNotDeleteCustomer() {
        //given
        Customer customer = new Customer("John", "Smith", "12345");
        customerRepository.save(customer);

        Currency pln = new Currency("PLN");
        currencyRepository.save(pln);

        LoanApplication application = new LoanApplication(LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"));
        repository.save(application);

        //when
        service.deleteById(application.getId());
        int customersSize = customerRepository.findAll().size();

        //then
        assertEquals(1, customersSize);
    }

    @Test
    public void shouldCreateLoanAfterAcceptingApplication() throws InvalidLoanStatusException,
            InvalidApplicationStatusException, LoanApplicationNotFoundException, CurrencyNotFoundException {
        //given
        Customer customer = new Customer("John", "Smith", "12345");
        customerRepository.save(customer);

        Currency pln = new Currency("PLN");
        currencyRepository.save(pln);

        LoanApplication application = new LoanApplication(LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"));
        service.save(application);
        int loansSizeBeforeAcceptingApplication = loanRepository.findAll().size();

        //when
        application.setStatus("accepted");
        service.save(application);
        int loansSizeAfterAcceptingApplication = loanRepository.findAll().size();

        //then
        assertEquals(0, loansSizeBeforeAcceptingApplication);
        assertEquals(1, loansSizeAfterAcceptingApplication);
    }

}
