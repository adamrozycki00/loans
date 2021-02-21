package com.tenetmind.loans.operation.domainmodel;

import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.repository.CurrencyRepository;
import com.tenetmind.loans.customer.domainmodel.Customer;
import com.tenetmind.loans.customer.repository.CustomerRepository;
import com.tenetmind.loans.loan.domainmodel.Loan;
import com.tenetmind.loans.loan.repository.LoanRepository;
import com.tenetmind.loans.application.domainmodel.LoanApplication;
import com.tenetmind.loans.application.repository.LoanApplicationRepository;
import com.tenetmind.loans.operation.repository.OperationRepository;
import com.tenetmind.loans.operation.service.OperationService;
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
public class OperationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private LoanApplicationRepository applicationRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private OperationRepository repository;

    @Autowired
    private OperationService service;

    @After
    public void cleanUp() {
        repository.deleteAll();
        loanRepository.deleteAll();
        applicationRepository.deleteAll();
        currencyRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void shouldCreateOperation() {
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

        Operation operation = new Operation(LocalDate.now(), loan, "Installment payment",
                pln, new BigDecimal("200"), new BigDecimal("200"));

        //when
        service.save(operation);
        int operationsSize = repository.findAll().size();

        //then
        assertEquals(1, operationsSize);
    }

    @Test
    public void shouldDeleteOperationAndNotDeleteCurrency() {
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

        Operation operation = new Operation(LocalDate.now(), loan, "Installment payment",
                pln, new BigDecimal("200"), new BigDecimal("200"));
        repository.save(operation);

        //when
        service.deleteById(operation.getId());
        int operationsSize = repository.findAll().size();
        int currenciesSize = currencyRepository.findAll().size();

        //then
        assertEquals(0, operationsSize);
        assertEquals(1, currenciesSize);
    }

    @Test
    public void shouldDeleteOperationAndNotDeleteLoan() {
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

        Operation operation = new Operation(LocalDate.now(), loan, "Installment payment",
                pln, new BigDecimal("200"), new BigDecimal("200"));
        repository.save(operation);

        //when
        service.deleteById(operation.getId());
        int operationsSize = repository.findAll().size();
        int loansSize = loanRepository.findAll().size();

        //then
        assertEquals(0, operationsSize);
        assertEquals(1, loansSize);
    }

}
