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

        Operation makingLoan = new Operation(date, loan, "Making loan", loan.getCurrency(),
                loan.getAmount(), amountInPln);
        operationService.save(makingLoan);

        loan.setBalance(loan.getAmount());
        loanService.save(loan);
    }

    public void payInstallment(LocalDate date, Loan loan, Currency currency, BigDecimal amount)
            throws CurrencyConversionException, PaymentAmountException {
        if (!validatePaymentAmount(date, loan, currency, amount)) {
            throw new PaymentAmountException();
        }

        BigDecimal amountInPln = converter.convertToPln(amount, currency, date);
        BigDecimal amountInLoanCurrency = converter.convert(amount, currency, loan.getCurrency(), date);

        Operation installmentPayment = new Operation(date, loan, "Installment payment", currency,
                amount, amountInPln);
        operationService.save(installmentPayment);

        loan.setBalance(loan.getBalance().subtract(amountInLoanCurrency));
        loanService.save(loan);
    }

    private boolean validatePaymentAmount(LocalDate date, Loan loan, Currency currency, BigDecimal amount)
            throws CurrencyConversionException {
        if (amount.compareTo(new BigDecimal("0")) <= 0) {
            return false;
        }

        BigDecimal amountInLoanCurrency = converter.convert(amount, currency, loan.getCurrency(), date);

        return amountInLoanCurrency.compareTo(loan.getBalance()) <= 0;
    }

}
