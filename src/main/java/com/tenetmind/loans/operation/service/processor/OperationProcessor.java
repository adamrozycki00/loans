package com.tenetmind.loans.operation.service.processor;

import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.service.converter.CurrencyConversionException;
import com.tenetmind.loans.currency.service.converter.CurrencyConverter;
import com.tenetmind.loans.loan.domainmodel.Loan;
import com.tenetmind.loans.loan.service.LoanService;
import com.tenetmind.loans.operation.domainmodel.Operation;
import com.tenetmind.loans.operation.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class OperationProcessor {

    @Autowired
    private CurrencyConverter converter;

    @Autowired
    private LoanService loanService;

    @Autowired
    private OperationService operationService;

    public void makeLoan(LocalDate date, Loan loan) throws CurrencyConversionException {
        BigDecimal amountInPln = converter.convertToPln(loan.getAmount(), loan.getCurrency(), date);

        Operation makingALoan = new Operation(date, loan, "Making a loan", loan.getCurrency(),
                loan.getAmount(), amountInPln);
        operationService.save(makingALoan);

        loan.setBalance(loan.getAmount());
        loanService.save(loan);
    }

    public void payInstallment(LocalDate date, Loan loan, Currency currency, BigDecimal amount)
            throws CurrencyConversionException {
        BigDecimal amountInPln = converter.convertToPln(amount, currency, date);

        Operation installmentPayment = new Operation(date, loan, "Installment payment", currency,
                amount, amountInPln);
        operationService.save(installmentPayment);

        BigDecimal amountInLoanCurrency = converter.convert(amount, currency, loan.getCurrency(), date);

        loan.setBalance(loan.getBalance().subtract(amountInLoanCurrency));
        loanService.save(loan);
    }

}