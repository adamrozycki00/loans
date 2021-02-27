package com.tenetmind.loans.installment.domainmodel;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.repository.CurrencyRepository;
import com.tenetmind.loans.customer.domainmodel.Customer;
import com.tenetmind.loans.customer.repository.CustomerRepository;
import com.tenetmind.loans.installment.repository.InstallmentRepository;
import com.tenetmind.loans.installment.service.InstallmentService;
import com.tenetmind.loans.loan.domainmodel.Loan;
import com.tenetmind.loans.loan.repository.LoanRepository;
import com.tenetmind.loans.application.domainmodel.LoanApplication;
import com.tenetmind.loans.application.repository.LoanApplicationRepository;
import org.junit.After;
import org.junit.Before;
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

    @Before
    public void setUp() {
        loanRepository.deleteAll();
        repository.deleteAll();
        applicationRepository.deleteAll();
        currencyRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @After
    public void cleanUp() {
        loanRepository.deleteAll();
        repository.deleteAll();
        applicationRepository.deleteAll();
        currencyRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void shouldCreateInstallment() throws CurrencyNotFoundException {
        //given
        Customer customer = new Customer("John", "Smith", "12345");
        customerRepository.save(customer);

        Currency pln = new Currency("PLN");
        currencyRepository.save(pln);

        LoanApplication application = new LoanApplication(LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"));
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

}
