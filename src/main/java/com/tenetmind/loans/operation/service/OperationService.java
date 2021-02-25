package com.tenetmind.loans.operation.service;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currency.service.converter.CurrencyConversionException;
import com.tenetmind.loans.loan.controller.LoanNotFoundException;
import com.tenetmind.loans.loan.domainmodel.Loan;
import com.tenetmind.loans.loan.service.InvalidLoanStatusException;
import com.tenetmind.loans.loan.service.LoanService;
import com.tenetmind.loans.operation.domainmodel.Operation;
import com.tenetmind.loans.operation.repository.OperationRepository;
import com.tenetmind.loans.operation.service.accounting.AccountingUnit;
import com.tenetmind.loans.operation.service.processor.OperationProcessor;
import com.tenetmind.loans.operation.service.processor.PaymentAmountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OperationService {

    @Autowired
    private OperationRepository repository;

    @Autowired
    private OperationProcessor processor;

    @Autowired
    private AccountingUnit accountingUnit;

    @Autowired
    private LoanService loanService;

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

    public void makeLoan(PaymentDto paymentDto) throws CurrencyNotFoundException, CurrencyConversionException,
            LoanNotFoundException, InvalidLoanStatusException {

        Operation makingLoan = processor.prepareMakingLoan(paymentDto);
        save(makingLoan);

        Loan madeLoan = accountingUnit.prepareSettlementOfMakingLoan(paymentDto);
        loanService.save(madeLoan);
    }

    public void payInstallment(PaymentDto paymentDto) throws CurrencyNotFoundException, CurrencyConversionException,
            PaymentAmountException, LoanNotFoundException, InvalidLoanStatusException {
        Operation installmentPayment = processor.prepareInstallmentPayment(paymentDto);
        save(installmentPayment);

        Loan loanAfterSettlementOfPayment = accountingUnit.prepareSettlementOfPayment(paymentDto);
        loanService.save(loanAfterSettlementOfPayment);
    }

}
