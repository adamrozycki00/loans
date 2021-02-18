package com.tenetmind.loans.loanapplication.domainmodel;

import com.tenetmind.loans.loan.domainmodel.Loan;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@Entity
@Table(name = "loan_applications")
@NoArgsConstructor
@Getter
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;
    private Currency currency;
    private BigDecimal amount;
    private Integer period;
    private BigDecimal marginRate;
    private String status;
    private Loan loan;

}
