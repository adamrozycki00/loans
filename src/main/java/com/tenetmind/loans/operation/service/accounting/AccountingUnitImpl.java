package com.tenetmind.loans.operation.service.accounting;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currencyrate.controller.CurrencyRateNotFoundException;
import com.tenetmind.loans.installment.domainmodel.Installment;
import com.tenetmind.loans.loan.controller.LoanNotFoundException;
import com.tenetmind.loans.loan.domainmodel.Loan;
import com.tenetmind.loans.loan.service.LoanService;
import com.tenetmind.loans.operation.service.PaymentDto;
import com.tenetmind.loans.operation.service.processor.OperationProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountingUnitImpl implements AccountingUnit {

    private static final String CLOSED = "Closed";

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

    public Loan prepareSettlementOfPayment(PaymentDto paymentDto) throws CurrencyNotFoundException,
            CurrencyRateNotFoundException, LoanNotFoundException {
        Loan loan = loanService.findById(paymentDto.getLoanId())
                .orElseThrow(LoanNotFoundException::new);

        BigDecimal paidAmountToSettle = processor.getAmountInLoanCurrency(paymentDto);
        BigDecimal amountPaidOutOfSchedule = getAmountPaidOutOfSchedule(loan);
        paidAmountToSettle = paidAmountToSettle.add(amountPaidOutOfSchedule);

        return processSettlementOfPayment(loan, paidAmountToSettle);
    }

    private Loan processSettlementOfPayment(Loan loan, BigDecimal amountToSettle) {
        List<Installment> schedule = loan.getSchedule();

        while (amountToSettle.compareTo(BigDecimal.ZERO) > 0
                && loan.getNumberOfInstallmentsPaid() < loan.getPeriod()) {
            int currentNumberOfInstallmentsSettled = loan.getNumberOfInstallmentsPaid();
            Installment nextInstallmentToSettle =
                    getNextInstallmentToSettle(schedule, currentNumberOfInstallmentsSettled);

            BigDecimal interestInNextInstallmentToSettle = nextInstallmentToSettle.getInterest();
            BigDecimal principalInNextInstallmentToSettle = nextInstallmentToSettle.getPrincipal();
            BigDecimal amountInNextInstallmentToSettle =
                    interestInNextInstallmentToSettle.add(principalInNextInstallmentToSettle);

            amountToSettle = settleNextInstallment(loan, amountToSettle, interestInNextInstallmentToSettle,
                    principalInNextInstallmentToSettle, amountInNextInstallmentToSettle);
        }

        if (loan.getAmountToPay().compareTo(BigDecimal.ZERO) == 0)
            loan.setStatus(CLOSED);

        return loan;
    }

    private BigDecimal settleNextInstallment(Loan loan, BigDecimal amountToSettle,
                                             BigDecimal interestInNextInstallmentToSettle,
                                             BigDecimal principalInNextInstallmentToSettle,
                                             BigDecimal amountInNextInstallmentToSettle) {
        if (amountToSettle.compareTo(amountInNextInstallmentToSettle) >= 0) {
            loan.setBalance(loan.getBalance().subtract(principalInNextInstallmentToSettle));
            loan.setAmountToPay(loan.getAmountToPay().subtract(amountInNextInstallmentToSettle));
            amountToSettle = amountToSettle.subtract(amountInNextInstallmentToSettle);
            loan.setNumberOfInstallmentsPaid(loan.getNumberOfInstallmentsPaid() + 1);
        } else if (amountToSettle.compareTo(interestInNextInstallmentToSettle) >= 0) {
            loan.setAmountToPay(loan.getAmountToPay().subtract(amountToSettle));
            amountToSettle = amountToSettle.subtract(interestInNextInstallmentToSettle);
            loan.setBalance(loan.getBalance().subtract(amountToSettle));
            amountToSettle = BigDecimal.ZERO;
        } else {
            loan.setAmountToPay(loan.getAmountToPay().subtract(amountToSettle));
            amountToSettle = BigDecimal.ZERO;
        }
        return amountToSettle;
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
