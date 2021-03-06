package com.tenetmind.loans.operation.service.processor;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.service.CurrencyService;
import com.tenetmind.loans.currencyrate.controller.CurrencyRateNotFoundException;
import com.tenetmind.loans.currencyrate.converter.CurrencyRateConverter;
import com.tenetmind.loans.loan.controller.LoanNotFoundException;
import com.tenetmind.loans.loan.domainmodel.Loan;
import com.tenetmind.loans.loan.service.LoanService;
import com.tenetmind.loans.operation.domainmodel.Operation;
import com.tenetmind.loans.operation.service.PaymentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_HALF_EVEN;

@Service
public class OperationProcessor {

    private CurrencyRateConverter converter;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private LoanService loanService;

    public CurrencyRateConverter getConverter() {
        return converter;
    }

    @Autowired
    @Qualifier("nbpRateConverter")
    public void setConverter(CurrencyRateConverter converter) {
        this.converter = converter;
    }

    public Operation prepareMakingLoan(PaymentDto paymentDto) throws CurrencyNotFoundException,
            CurrencyRateNotFoundException, LoanNotFoundException {
        Loan loan = loanService.findById(paymentDto.getLoanId())
                .orElseThrow(LoanNotFoundException::new);

        BigDecimal amountInPln =
                converter.convertToPln(loan.getAmount(), loan.getCurrency().getName(), paymentDto.getDate())
                        .setScale(2, ROUND_HALF_EVEN);

        return new Operation(paymentDto.getDate(), loan, "Making loan", loan.getCurrency(),
                loan.getAmount(), amountInPln);
    }

    public Operation prepareInstallmentPayment(PaymentDto paymentDto)
            throws CurrencyNotFoundException, CurrencyRateNotFoundException,
            PaymentAmountException, LoanNotFoundException {
        if (paymentAmountIsIncorrect(paymentDto)) {
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
            throws CurrencyNotFoundException, CurrencyRateNotFoundException, LoanNotFoundException {
        Loan loan = loanService.findById(paymentDto.getLoanId())
                .orElseThrow(LoanNotFoundException::new);

        return converter.convert(paymentDto.getAmount().setScale(2, ROUND_HALF_EVEN), paymentDto.getCurrencyName(),
                loan.getCurrency().getName(), paymentDto.getDate())
                .setScale(2, ROUND_HALF_EVEN);
    }

    private boolean paymentAmountIsIncorrect(PaymentDto paymentDto) {
        return paymentDto.getAmount().setScale(2, ROUND_HALF_EVEN).compareTo(BigDecimal.ZERO) <= 0;
    }

}
