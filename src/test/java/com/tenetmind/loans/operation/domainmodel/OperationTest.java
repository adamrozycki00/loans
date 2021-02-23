package com.tenetmind.loans.operation.domainmodel;

import com.tenetmind.loans.application.service.InvalidApplicationStatusException;
import com.tenetmind.loans.application.service.LoanApplicationService;
import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.repository.CurrencyRepository;
import com.tenetmind.loans.currency.service.converter.CurrencyConversionException;
import com.tenetmind.loans.customer.domainmodel.Customer;
import com.tenetmind.loans.customer.repository.CustomerRepository;
import com.tenetmind.loans.loan.controller.LoanNotFoundException;
import com.tenetmind.loans.loan.domainmodel.Loan;
import com.tenetmind.loans.loan.repository.LoanRepository;
import com.tenetmind.loans.application.domainmodel.LoanApplication;
import com.tenetmind.loans.application.repository.LoanApplicationRepository;
import com.tenetmind.loans.loan.service.InvalidLoanStatusException;
import com.tenetmind.loans.loan.service.LoanService;
import com.tenetmind.loans.operation.repository.OperationRepository;
import com.tenetmind.loans.operation.service.OperationService;
import com.tenetmind.loans.operation.service.PaymentDto;
import com.tenetmind.loans.operation.service.processor.PaymentAmountException;
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
public class OperationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private LoanApplicationRepository applicationRepository;

    @Autowired
    private LoanApplicationService applicationService;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanService loanService;

    @Autowired
    private OperationRepository repository;

    @Autowired
    private OperationService service;

    @Before
    public void setUp() {
        repository.deleteAll();
        loanRepository.deleteAll();
        applicationRepository.deleteAll();
        currencyRepository.deleteAll();
        customerRepository.deleteAll();
    }

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
                new BigDecimal("1000"), 12, new BigDecimal(".05"));
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
                new BigDecimal("1000"), 12, new BigDecimal(".05"));
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
                new BigDecimal("1000"), 12, new BigDecimal(".05"));
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

    @Test
    public void shouldChangeLoanBalanceAfterMakingLoan() throws InvalidLoanStatusException, InvalidApplicationStatusException, PaymentAmountException, LoanNotFoundException, CurrencyNotFoundException, CurrencyConversionException {
        //given
        Customer customer = new Customer("John", "Smith");
        customerRepository.save(customer);

        Currency pln = new Currency("PLN");
        currencyRepository.save(pln);

        LoanApplication application = new LoanApplication(LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"));
        application.setStatus("accepted");
        applicationService.save(application);

        Loan createdLoan = loanService.find(application).get();
        double balanceBeforeMakingLoan = createdLoan.getBalance().doubleValue();
        PaymentDto paymentDto = new PaymentDto(LocalDate.now(), createdLoan.getId());

        //when
        service.makeLoan(paymentDto);
        Loan madeLoan = loanService.find(application).get();
        double balanceAfterMakingLoan = madeLoan.getBalance().doubleValue();

        //then
        assertEquals(0, balanceBeforeMakingLoan, .001);
        assertEquals(1000, balanceAfterMakingLoan, .001);
    }

    @Test
    public void shouldChangeLoanStatusAfterMakingLoan() throws InvalidLoanStatusException,
            InvalidApplicationStatusException, PaymentAmountException, LoanNotFoundException,
            CurrencyNotFoundException, CurrencyConversionException {
        //given
        Customer customer = new Customer("John", "Smith");
        customerRepository.save(customer);

        Currency pln = new Currency("PLN");
        currencyRepository.save(pln);

        LoanApplication application = new LoanApplication(LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"));
        application.setStatus("accepted");
        applicationService.save(application);

        Loan createdLoan = loanService.find(application).get();
        String statusBeforeMakingLoan = createdLoan.getStatus();
        PaymentDto paymentDto = new PaymentDto(LocalDate.now(), createdLoan.getId());

        //when
        service.makeLoan(paymentDto);
        Loan madeLoan = loanService.find(application).get();
        String statusAfterMakingLoan = madeLoan.getStatus();

        //then
        assertEquals("New", statusBeforeMakingLoan);
        assertEquals("Active", statusAfterMakingLoan);
    }

    @Test
    public void shouldChangeLoanBalanceAfterPayingInstallment() throws InvalidLoanStatusException,
            InvalidApplicationStatusException, PaymentAmountException, LoanNotFoundException,
            CurrencyNotFoundException, CurrencyConversionException {
        //given
        Customer customer = new Customer("John", "Smith");
        customerRepository.save(customer);

        Currency pln = new Currency("PLN");
        currencyRepository.save(pln);

        LoanApplication application = new LoanApplication(LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"));
        application.setStatus("accepted");
        applicationService.save(application);

        Loan createdLoan = loanService.find(application).get();
        PaymentDto initialPaymentDto = new PaymentDto(LocalDate.now(), createdLoan.getId());
        service.makeLoan(initialPaymentDto);
        Loan madeLoan = loanService.find(application).get();
        double balanceBeforePayingInstallment = madeLoan.getBalance().doubleValue();

        //when
        PaymentDto installmentPaymentDto = new PaymentDto(LocalDate.now(), madeLoan.getId(),
                "PLN", new BigDecimal("100.00"));
        service.payInstallment(installmentPaymentDto);
        Loan paidLoan = loanService.find(application).get();
        double balanceAfterPayingInstallment = paidLoan.getBalance().doubleValue();

        //then
        assertEquals(1000, balanceBeforePayingInstallment, .001);
        assertEquals(900, balanceAfterPayingInstallment, .001);
    }

}
