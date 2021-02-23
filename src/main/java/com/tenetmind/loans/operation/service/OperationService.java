package com.tenetmind.loans.operation.service;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.service.CurrencyService;
import com.tenetmind.loans.currency.service.converter.CurrencyConversionException;
import com.tenetmind.loans.installment.domainmodel.Installment;
import com.tenetmind.loans.installment.service.InstallmentService;
import com.tenetmind.loans.installment.service.interestcalc.InterestCalc;
import com.tenetmind.loans.loan.controller.LoanNotFoundException;
import com.tenetmind.loans.loan.domainmodel.Loan;
import com.tenetmind.loans.loan.service.InvalidLoanStatusException;
import com.tenetmind.loans.loan.service.LoanService;
import com.tenetmind.loans.operation.domainmodel.Operation;
import com.tenetmind.loans.operation.repository.OperationRepository;
import com.tenetmind.loans.operation.service.processor.OperationProcessor;
import com.tenetmind.loans.operation.service.processor.PaymentAmountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
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
    private InstallmentService installmentService;

    @Autowired
    private InterestCalc interestCalc;

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
        save(processor.prepareMakingLoan(paymentDto));

        Loan loan = loanService.findById(paymentDto.getLoanId())
                .orElseThrow(LoanNotFoundException::new);
        loan.setBalance(loan.getAmount());
        loan.setStatus("Active");

        loanService.save(loan);

        installmentService.makeSchedule(loan, interestCalc);
    }

    public void payInstallment(PaymentDto paymentDto) throws CurrencyNotFoundException, CurrencyConversionException,
            PaymentAmountException, LoanNotFoundException, InvalidLoanStatusException {
        save(processor.prepareInstallmentPayment(paymentDto));

        Loan loan = loanService.findById(paymentDto.getLoanId())
                .orElseThrow(LoanNotFoundException::new);
        BigDecimal amountInLoanCurrency = processor.getAmountInLoanCurrency(paymentDto);
        loan.setBalance(loan.getBalance().subtract(amountInLoanCurrency));
        loanService.save(loan);
    }

}
