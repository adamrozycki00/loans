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

import static java.math.BigDecimal.ROUND_HALF_EVEN;

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

        BigDecimal balanceAfterOperation = loan.getAmount();
        balanceAfterOperation = balanceAfterOperation.setScale(2, ROUND_HALF_EVEN);

        loan.setBalance(balanceAfterOperation);
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

        BigDecimal balanceAfterOperation = loan.getBalance().subtract(amountInLoanCurrency);
        balanceAfterOperation = balanceAfterOperation.setScale(2, ROUND_HALF_EVEN);

        loan.setBalance(balanceAfterOperation);
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
