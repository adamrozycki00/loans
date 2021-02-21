package com.tenetmind.loans.loan.domainmodel;

import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.repository.CurrencyRepository;
import com.tenetmind.loans.customer.domainmodel.Customer;
import com.tenetmind.loans.customer.repository.CustomerRepository;
import com.tenetmind.loans.loan.repository.LoanRepository;
import com.tenetmind.loans.loan.service.LoanService;
import com.tenetmind.loans.loanapplication.domainmodel.LoanApplication;
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
public class LoanTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private LoanApplicationRepository applicationRepository;

    @Autowired
    private LoanRepository repository;

    @Autowired
    private LoanService service;

    @After
    public void cleanUp() {
        repository.deleteAll();
        applicationRepository.deleteAll();
        currencyRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void shouldCreateLoan() {
        //given
        Customer customer = new Customer("John", "Smith");
        customerRepository.save(customer);

        Currency pln = new Currency("PLN");
        currencyRepository.save(pln);

        LoanApplication application = new LoanApplication(LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"), "New");
        applicationRepository.save(application);

        Loan loan = new Loan(LocalDateTime.now(), application, new BigDecimal(".05"));

        //when
        service.save(loan);
        int loansSize = repository.findAll().size();

        //then
        assertEquals(1, loansSize);
    }

    @Test
    public void shouldDeleteLoanAndNotDeleteCustomer() {
        //given
        Customer customer = new Customer("John", "Smith");
        customerRepository.save(customer);

        Currency pln = new Currency("PLN");
        currencyRepository.save(pln);

        LoanApplication application = new LoanApplication(LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"), "New");
        applicationRepository.save(application);

        Loan loan = new Loan(LocalDateTime.now(), application, new BigDecimal(".05"));
        repository.save(loan);

        //when
        service.deleteById(loan.getId());
        int loansSize = repository.findAll().size();
        int customersSize = customerRepository.findAll().size();

        //then
        assertEquals(0, loansSize);
        assertEquals(1, customersSize);
    }

    @Test
    public void shouldDeleteLoanAndNotDeleteCurrency() {
        //given
        Customer customer = new Customer("John", "Smith");
        customerRepository.save(customer);

        Currency pln = new Currency("PLN");
        currencyRepository.save(pln);

        LoanApplication application = new LoanApplication(LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"), "New");
        applicationRepository.save(application);

        Loan loan = new Loan(LocalDateTime.now(), application, new BigDecimal(".05"));
        repository.save(loan);

        //when
        service.deleteById(loan.getId());
        int loansSize = repository.findAll().size();
        int currenciesSize = currencyRepository.findAll().size();

        //then
        assertEquals(0, loansSize);
        assertEquals(1, currenciesSize);
    }

    @Test
    public void shouldDeleteLoanAndNotDeleteLoanApplication() {
        //given
        Customer customer = new Customer("John", "Smith");
        customerRepository.save(customer);

        Currency pln = new Currency("PLN");
        currencyRepository.save(pln);

        LoanApplication application = new LoanApplication(LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"), "New");
        applicationRepository.save(application);

        Loan loan = new Loan(LocalDateTime.now(), application, new BigDecimal(".05"));
        repository.save(loan);

        //when
        service.deleteById(loan.getId());
        int loansSize = repository.findAll().size();
        int applicationsSize = applicationRepository.findAll().size();

        //then
        assertEquals(0, loansSize);
        assertEquals(1, applicationsSize);
    }

}
