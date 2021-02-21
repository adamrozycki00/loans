package com.tenetmind.loans.operation.domainmodel;

import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.loan.domainmodel.Loan;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "operations")
@NoArgsConstructor
@Getter
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;

    private String type;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    private BigDecimal amount;
    private BigDecimal plnAmount;

    public Operation(LocalDate date, Loan loan, String type, Currency currency,
                     BigDecimal amount, BigDecimal plnAmount) {
        this.date = date;
        this.loan = loan;
        this.type = type;
        this.currency = currency;
        this.amount = amount;
        this.plnAmount = plnAmount;
    }

}
