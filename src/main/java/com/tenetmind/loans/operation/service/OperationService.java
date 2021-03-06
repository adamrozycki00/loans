package com.tenetmind.loans.operation.service;

import com.tenetmind.loans.application.controller.LoanApplicationNotFoundException;
import com.tenetmind.loans.application.service.InvalidApplicationStatusException;
import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.service.CurrencyService;
import com.tenetmind.loans.currencyrate.controller.CurrencyRateNotFoundException;
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

    @Autowired
    private CurrencyService currencyService;

    public List<Operation> findAll() {
        return repository.findAll();
    }

    public Optional<Operation> findById(Long id) {
        if (id == null) return Optional.empty();
        return repository.findById(id);
    }

    public Operation save(Operation operation) throws LoanNotFoundException, CurrencyNotFoundException {

        Loan loan = operation.getLoan();
        Optional<Loan> retrievedLoan = loanService.findById(loan.getId());
        if (retrievedLoan.isPresent()) {
            operation.setLoan(retrievedLoan.get());
        } else {
            throw new LoanNotFoundException();
        }

        Currency currency = loan.getCurrency();
        Optional<Currency> retrievedCurrency = currencyService.find(currency.getName());
        if (retrievedCurrency.isPresent()) {
            loan.setCurrency(retrievedCurrency.get());
        } else {
            throw new CurrencyNotFoundException();
        }

        return repository.save(operation);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void makeLoan(PaymentDto paymentDto) throws CurrencyNotFoundException, CurrencyRateNotFoundException,
            LoanNotFoundException, InvalidLoanStatusException, LoanApplicationNotFoundException,
            InvalidApplicationStatusException {

        Operation makingLoan = processor.prepareMakingLoan(paymentDto);
        save(makingLoan);

        Loan loanAfterSettlementOfMakingLoan = accountingUnit.prepareSettlementOfMakingLoan(paymentDto);
        loanService.save(loanAfterSettlementOfMakingLoan);
    }

    public void payInstallment(PaymentDto paymentDto) throws CurrencyNotFoundException, CurrencyRateNotFoundException,
            PaymentAmountException, LoanNotFoundException, InvalidLoanStatusException,
            LoanApplicationNotFoundException, InvalidApplicationStatusException {
        Operation installmentPayment = processor.prepareInstallmentPayment(paymentDto);
        save(installmentPayment);

        Loan loanAfterSettlementOfPayment = accountingUnit.prepareSettlementOfPayment(paymentDto);
        loanService.save(loanAfterSettlementOfPayment);
    }

}
