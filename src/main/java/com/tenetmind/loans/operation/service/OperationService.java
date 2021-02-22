package com.tenetmind.loans.operation.service;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.service.CurrencyService;
import com.tenetmind.loans.currency.service.converter.CurrencyConversionException;
import com.tenetmind.loans.loan.controller.LoanNotFoundException;
import com.tenetmind.loans.loan.domainmodel.Loan;
import com.tenetmind.loans.loan.service.LoanService;
import com.tenetmind.loans.operation.domainmodel.Operation;
import com.tenetmind.loans.operation.repository.OperationRepository;
import com.tenetmind.loans.operation.service.processor.OperationProcessor;
import com.tenetmind.loans.operation.service.processor.PaymentAmountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OperationService {

    @Autowired
    private OperationRepository repository;

    @Autowired
    private OperationProcessor processor;

    @Autowired
    private LoanService loanService;

    @Autowired
    private CurrencyService currencyService;

    public List<Operation> findAll() {
        return repository.findAll();
    }

    public Optional<Operation> findById(Long id) {
        return repository.findById(id);
    }

    public Operation save(Operation operation) {
        return repository.save(operation);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void makeLoan(LocalDate date, Loan loan) throws CurrencyConversionException {
        processor.makeLoan(date, loan);
    }

    public void makeLoan(Long loanId) throws CurrencyConversionException, LoanNotFoundException {
        Loan loan = loanService.findById(loanId).orElseThrow(LoanNotFoundException::new);
        makeLoan(LocalDate.now(), loan);
    }

    public void makeLoan(LocalDate date, Long loanId) throws CurrencyConversionException, LoanNotFoundException {
        Loan loan = loanService.findById(loanId).orElseThrow(LoanNotFoundException::new);
        makeLoan(date, loan);
    }

    public void payInstallment(LocalDate date, Loan loan, Currency currency, BigDecimal amount)
            throws CurrencyConversionException, PaymentAmountException {
        processor.payInstallment(date, loan, currency, amount);
    }

    public void payInstallment(Long loanId, BigDecimal amount)
            throws CurrencyConversionException, PaymentAmountException, LoanNotFoundException {
        Loan loan = loanService.findById(loanId).orElseThrow(LoanNotFoundException::new);
        payInstallment(LocalDate.now(), loan, loan.getCurrency(), amount);
    }

    public void payInstallment(LocalDate date, Long loanId, BigDecimal amount)
            throws CurrencyConversionException, PaymentAmountException, LoanNotFoundException {
        Loan loan = loanService.findById(loanId).orElseThrow(LoanNotFoundException::new);
        payInstallment(date, loan, loan.getCurrency(), amount);
    }

    public void payInstallment(LocalDate date, Long loanId, String currencyName, BigDecimal amount)
            throws CurrencyConversionException, PaymentAmountException,
            LoanNotFoundException, CurrencyNotFoundException {
        Loan loan = loanService.findById(loanId).orElseThrow(LoanNotFoundException::new);
        Currency currency = currencyService.find(currencyName).orElseThrow(CurrencyNotFoundException::new);
        payInstallment(date, loan, currency, amount);
    }

}
