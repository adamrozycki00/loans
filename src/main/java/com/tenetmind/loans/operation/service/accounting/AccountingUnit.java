package com.tenetmind.loans.operation.service.accounting;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currencyrate.converter.CurrencyRateConversionException;
import com.tenetmind.loans.installment.domainmodel.Installment;
import com.tenetmind.loans.loan.controller.LoanNotFoundException;
import com.tenetmind.loans.loan.domainmodel.Loan;
import com.tenetmind.loans.loan.service.LoanService;
import com.tenetmind.loans.operation.service.PaymentDto;
import com.tenetmind.loans.operation.service.processor.OperationProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountingUnit {

    @Autowired
    private OperationProcessor processor;

    @Autowired
    private LoanService loanService;

    public Loan prepareSettlementOfMakingLoan(PaymentDto paymentDto) throws LoanNotFoundException {
        Loan loan = loanService.findById(paymentDto.getLoanId())
                .orElseThrow(LoanNotFoundException::new);

        loan.setBalance(loan.getAmount());
        loan.setAmountToPay(getInitialAmountToPay(loan));
        loan.setStatus("Active");

        return loan;
    }

    public Loan prepareSettlementOfPayment(PaymentDto paymentDto) throws CurrencyRateConversionException,
            CurrencyNotFoundException, LoanNotFoundException {
        Loan loan = loanService.findById(paymentDto.getLoanId())
                .orElseThrow(LoanNotFoundException::new);

        BigDecimal paidAmountToSettle = processor.getAmountInLoanCurrency(paymentDto);
        BigDecimal amountPaidOutOfSchedule = getAmountPaidOutOfSchedule(loan);
        paidAmountToSettle = paidAmountToSettle.add(amountPaidOutOfSchedule);

        List<Installment> schedule = loan.getSchedule();

        while (paidAmountToSettle.compareTo(BigDecimal.ZERO) > 0
                && loan.getNumberOfInstallmentsPaid() < loan.getPeriod()) {
            int currentNumberOfInstallmentsSettled = loan.getNumberOfInstallmentsPaid();
            Installment nextInstallmentToSettle =
                    getNextInstallmentToSettle(schedule, currentNumberOfInstallmentsSettled);

            BigDecimal interestInNextInstallmentToSettle = nextInstallmentToSettle.getInterest();
            BigDecimal principalInNextInstallmentToSettle = nextInstallmentToSettle.getPrincipal();
            BigDecimal amountInNextInstallmentToSettle =
                    interestInNextInstallmentToSettle.add(principalInNextInstallmentToSettle);

            if (paidAmountToSettle.compareTo(amountInNextInstallmentToSettle) >= 0) {
                loan.setBalance(loan.getBalance().subtract(principalInNextInstallmentToSettle));
                loan.setAmountToPay(loan.getAmountToPay().subtract(amountInNextInstallmentToSettle));
                paidAmountToSettle = paidAmountToSettle.subtract(amountInNextInstallmentToSettle);
                loan.setNumberOfInstallmentsPaid(loan.getNumberOfInstallmentsPaid() + 1);
            } else if (paidAmountToSettle.compareTo(interestInNextInstallmentToSettle) >= 0) {
                loan.setAmountToPay(loan.getAmountToPay().subtract(paidAmountToSettle));
                paidAmountToSettle = paidAmountToSettle.subtract(interestInNextInstallmentToSettle);
                loan.setBalance(loan.getBalance().subtract(paidAmountToSettle));
                paidAmountToSettle = BigDecimal.ZERO;
            } else {
                loan.setAmountToPay(loan.getAmountToPay().subtract(paidAmountToSettle));
                paidAmountToSettle = BigDecimal.ZERO;
            }
        }

        return loan;
    }

    private Installment getNextInstallmentToSettle(List<Installment> schedule, int currentNumberOfInstallmentsSettled) {
        return schedule.stream()
                .filter(installment -> installment.getNumber() == currentNumberOfInstallmentsSettled + 1)
                .collect(Collectors.toList())
                .get(0);
    }

    private BigDecimal getInitialAmountToPay(Loan loan) {
        return loan.getSchedule().stream()
                .map(installment -> installment.getPrincipal().add(installment.getInterest()))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    private BigDecimal getAmountPaidOutOfSchedule(Loan loan) {
        BigDecimal scheduledAmountToPay = loan.getSchedule().stream()
                .filter(installment -> installment.getNumber() > loan.getNumberOfInstallmentsPaid())
                .map(installment -> installment.getPrincipal().add(installment.getInterest()))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        return scheduledAmountToPay.subtract(loan.getAmountToPay());
    }

}
