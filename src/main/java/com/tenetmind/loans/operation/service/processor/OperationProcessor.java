package com.tenetmind.loans.operation.service.processor;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.service.CurrencyService;
import com.tenetmind.loans.currency.service.converter.CurrencyConversionException;
import com.tenetmind.loans.currency.service.converter.CurrencyConverter;
import com.tenetmind.loans.installment.domainmodel.Installment;
import com.tenetmind.loans.loan.controller.LoanNotFoundException;
import com.tenetmind.loans.loan.domainmodel.Loan;
import com.tenetmind.loans.loan.service.LoanService;
import com.tenetmind.loans.operation.domainmodel.Operation;
import com.tenetmind.loans.operation.service.PaymentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_HALF_EVEN;

@Service
public class OperationProcessor {

    @Autowired
    private CurrencyConverter converter;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private LoanService loanService;

    public Operation prepareMakingLoan(PaymentDto paymentDto) throws CurrencyNotFoundException,
            CurrencyConversionException, LoanNotFoundException {
        Loan loan = loanService.findById(paymentDto.getLoanId())
                .orElseThrow(LoanNotFoundException::new);

        BigDecimal amountInPln =
                converter.convertToPln(loan.getAmount(), loan.getCurrency().getName(), paymentDto.getDate())
                        .setScale(2, ROUND_HALF_EVEN);

        return new Operation(paymentDto.getDate(), loan, "Making loan", loan.getCurrency(),
                loan.getAmount(), amountInPln);
    }

    public Operation prepareInstallmentPayment(PaymentDto paymentDto)
            throws CurrencyNotFoundException, CurrencyConversionException,
            PaymentAmountException, LoanNotFoundException {
        if (incorrectPaymentAmount(paymentDto)) {
            throw new PaymentAmountException();
        }

        BigDecimal amount = paymentDto.getAmount().setScale(2, ROUND_HALF_EVEN);

        BigDecimal amountInPln = converter.convertToPln(amount, paymentDto.getCurrencyName(), paymentDto.getDate())
                .setScale(2, ROUND_HALF_EVEN);

        Loan loan = loanService.findById(paymentDto.getLoanId())
                .orElseThrow(LoanNotFoundException::new);

        Currency currency = currencyService.find(paymentDto.getCurrencyName())
                .orElseThrow(CurrencyNotFoundException::new);

        return new Operation(paymentDto.getDate(), loan, "Installment payment", currency, amount, amountInPln);
    }

    public BigDecimal getAmountInLoanCurrency(PaymentDto paymentDto)
            throws CurrencyNotFoundException, CurrencyConversionException, LoanNotFoundException {
        Loan loan = loanService.findById(paymentDto.getLoanId())
                .orElseThrow(LoanNotFoundException::new);

        return converter.convert(paymentDto.getAmount().setScale(2, ROUND_HALF_EVEN), paymentDto.getCurrencyName(),
                loan.getCurrency().getName(), paymentDto.getDate())
                .setScale(2, ROUND_HALF_EVEN);
    }

    private boolean incorrectPaymentAmount(PaymentDto paymentDto) throws LoanNotFoundException {
        Loan loan = loanService.findById(paymentDto.getLoanId())
                .orElseThrow(LoanNotFoundException::new);

        return paymentDto.getAmount().setScale(2, ROUND_HALF_EVEN).compareTo(new BigDecimal("0")) <= 0;
    }

    public BigDecimal getInitialAmountToPay(Loan loan) {
        return loan.getSchedule().stream()
                .map(installment -> installment.getPrincipal().add(installment.getInterest()))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    public BigDecimal getAmountPaidOutOfSchedule(Loan loan) {
        BigDecimal scheduledAmountToPay = loan.getSchedule().stream()
                .filter(installment -> installment.getNumber() > loan.getNumberOfInstallmentsPaid())
                .map(installment -> installment.getPrincipal().add(installment.getInterest()))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        return scheduledAmountToPay.subtract(loan.getAmountToPay());
    }

    public BigDecimal getBalancePaidOutOfSchedule(Loan loan) {
        BigDecimal scheduledBalance = loan.getSchedule().stream()
                .filter(installment -> installment.getNumber() > loan.getNumberOfInstallmentsPaid())
                .map(Installment::getPrincipal)
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        return scheduledBalance.subtract(loan.getBalance());
    }


    public BigDecimal getAmountToPayLeftAfterPayment(PaymentDto paymentDto) throws CurrencyConversionException,
            CurrencyNotFoundException, LoanNotFoundException {
        Loan loan = loanService.findById(paymentDto.getLoanId()).orElseThrow(LoanNotFoundException::new);
        BigDecimal currentAmountToPay = loan.getAmountToPay();
        BigDecimal paymentAmountInLoanCurrency = getAmountInLoanCurrency(paymentDto);
        return currentAmountToPay.subtract(paymentAmountInLoanCurrency);
    }

    public Loan settleLoanPayment(PaymentDto paymentDto) throws CurrencyConversionException,
            CurrencyNotFoundException, LoanNotFoundException {
        Loan loan = loanService.findById(paymentDto.getLoanId()).orElseThrow(LoanNotFoundException::new);
        int numberOfInstallmentsSettled = loan.getNumberOfInstallmentsPaid();
        BigDecimal loanBalance = loan.getAmount();
        BigDecimal interestRate = loan.getBaseRate().add(loan.getMarginRate());

        BigDecimal amountToSettle = getAmountInLoanCurrency(paymentDto);
        BigDecimal amountPaidOutOfSchedule = getAmountPaidOutOfSchedule(loan);
        BigDecimal balancePaidOutOfSchedule = getBalancePaidOutOfSchedule(loan);

        amountToSettle = amountToSettle.add(amountPaidOutOfSchedule);

        while (amountToSettle.compareTo(BigDecimal.ZERO) > 0 && numberOfInstallmentsSettled > 0) {

        }


        return null;
    }

}
