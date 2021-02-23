package com.tenetmind.loans.installment.service.interestcalc;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_HALF_EVEN;

@Service
public class InterestCalc {

    public BigDecimal calculatePrincipal(int installmentsLeft, BigDecimal loanBalance,
                                         BigDecimal interestRate) {
        return loanBalance.divide(new BigDecimal(installmentsLeft), 2, ROUND_HALF_EVEN);
    }

    public BigDecimal calculateInterest(int installmentsLeft, BigDecimal loanBalance,
                                        BigDecimal interestRate) {
        return loanBalance
                .multiply(interestRate)
                .divide(new BigDecimal(12), 2, ROUND_HALF_EVEN);
    }

}
