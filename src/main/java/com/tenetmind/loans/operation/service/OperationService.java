package com.tenetmind.loans.operation.service;

import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.service.converter.CurrencyConversionException;
import com.tenetmind.loans.loan.domainmodel.Loan;
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

    public void payInstallment(LocalDate date, Loan loan, Currency currency, BigDecimal amount)
            throws CurrencyConversionException, PaymentAmountException {
        processor.payInstallment(date, loan, currency, amount);
    }

}
