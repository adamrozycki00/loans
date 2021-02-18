package com.tenetmind.loans.loanapplication.domainmodel;

import com.tenetmind.loans.loan.domainmodel.Loan;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@AllArgsConstructor
@Getter
public class LoanApplicationDto {

    private Long id;
    private LocalDateTime date;
    private Currency currency;
    private BigDecimal amount;
    private Integer period;
    private BigDecimal marginRate;
    private String status;
    private Loan loan;

}
