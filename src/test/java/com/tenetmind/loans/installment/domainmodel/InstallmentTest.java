package com.tenetmind.loans.installment.domainmodel;

import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.repository.CurrencyRepository;
import com.tenetmind.loans.customer.domainmodel.Customer;
import com.tenetmind.loans.customer.repository.CustomerRepository;
import com.tenetmind.loans.installment.repository.InstallmentRepository;
import com.tenetmind.loans.installment.service.InstallmentService;
import com.tenetmind.loans.loan.domainmodel.Loan;
import com.tenetmind.loans.loan.repository.LoanRepository;
import com.tenetmind.loans.loanapplication.domainmodel.LoanApplication;
import com.tenetmind.loans.loanapplication.repository.LoanApplicationRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InstallmentTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private LoanApplicationRepository applicationRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private InstallmentRepository repository;

    @Autowired
    private InstallmentService service;

    @After
    public void cleanUp() {
        repository.deleteAll();
        loanRepository.deleteAll();
        applicationRepository.deleteAll();
        currencyRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void shouldCreateInstallment() {
        //given
        Customer customer = new Customer("John", "Smith");
        customerRepository.save(customer);

        Currency pln = new Currency("PLN");
        currencyRepository.save(pln);

        LoanApplication application = new LoanApplication(LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"), "New");
        applicationRepository.save(application);

        Loan loan = new Loan(LocalDateTime.now(), application, new BigDecimal(".05"));
        loanRepository.save(loan);

        Installment installment = new Installment(LocalDate.now(), loan, 1,
                new BigDecimal("100"), new BigDecimal("100"));

        //when
        service.save(installment);
        int installmentsSize = repository.findAll().size();

        //then
        assertEquals(1, installmentsSize);
    }

    @Test
    public void shouldDeleteInstallmentAndNotDeleteCustomer() {
        //given
        Customer customer = new Customer("John", "Smith");
        customerRepository.save(customer);

        Currency pln = new Currency("PLN");
        currencyRepository.save(pln);

        LoanApplication application = new LoanApplication(LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"), "New");
        applicationRepository.save(application);

        Loan loan = new Loan(LocalDateTime.now(), application, new BigDecimal(".05"));
        loanRepository.save(loan);

        Installment installment = new Installment(LocalDate.now(), loan, 1,
                new BigDecimal("100"), new BigDecimal("100"));
        repository.save(installment);

        //when
        service.deleteById(installment.getId());
        int installmentsSize = repository.findAll().size();
        int customersSize = customerRepository.findAll().size();

        //then
        assertEquals(0, installmentsSize);
        assertEquals(1, customersSize);
    }

    @Test
    public void shouldDeleteInstallmentAndNotDeleteCurrency() {
        //given
        Customer customer = new Customer("John", "Smith");
        customerRepository.save(customer);

        Currency pln = new Currency("PLN");
        currencyRepository.save(pln);

        LoanApplication application = new LoanApplication(LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"), "New");
        applicationRepository.save(application);

        Loan loan = new Loan(LocalDateTime.now(), application, new BigDecimal(".05"));
        loanRepository.save(loan);

        Installment installment = new Installment(LocalDate.now(), loan, 1,
                new BigDecimal("100"), new BigDecimal("100"));
        repository.save(installment);

        //when
        service.deleteById(installment.getId());
        int installmentsSize = repository.findAll().size();
        int currenciesSize = currencyRepository.findAll().size();

        //then
        assertEquals(0, installmentsSize);
        assertEquals(1, currenciesSize);
    }

    @Test
    public void shouldDeleteInstallmentAndNotDeleteApplication() {
        //given
        Customer customer = new Customer("John", "Smith");
        customerRepository.save(customer);

        Currency pln = new Currency("PLN");
        currencyRepository.save(pln);

        LoanApplication application = new LoanApplication(LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"), "New");
        applicationRepository.save(application);

        Loan loan = new Loan(LocalDateTime.now(), application, new BigDecimal(".05"));
        loanRepository.save(loan);

        Installment installment = new Installment(LocalDate.now(), loan, 1,
                new BigDecimal("100"), new BigDecimal("100"));
        repository.save(installment);

        //when
        service.deleteById(installment.getId());
        int installmentsSize = repository.findAll().size();
        int applicationsSize = customerRepository.findAll().size();

        //then
        assertEquals(0, installmentsSize);
        assertEquals(1, applicationsSize);
    }

    @Test
    public void shouldDeleteInstallmentAndNotDeleteLoan() {
        //given
        Customer customer = new Customer("John", "Smith");
        customerRepository.save(customer);

        Currency pln = new Currency("PLN");
        currencyRepository.save(pln);

        LoanApplication application = new LoanApplication(LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"), "New");
        applicationRepository.save(application);

        Loan loan = new Loan(LocalDateTime.now(), application, new BigDecimal(".05"));
        loanRepository.save(loan);

        Installment installment = new Installment(LocalDate.now(), loan, 1,
                new BigDecimal("100"), new BigDecimal("100"));
        repository.save(installment);

        //when
        service.deleteById(installment.getId());
        int installmentsSize = repository.findAll().size();
        int loansSize = loanRepository.findAll().size();

        //then
        assertEquals(0, installmentsSize);
        assertEquals(1, loansSize);
    }

}
