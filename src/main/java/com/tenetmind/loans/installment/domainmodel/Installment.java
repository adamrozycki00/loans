package com.tenetmind.loans.installment.domainmodel;

import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.loan.domainmodel.Loan;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "installments")
@NoArgsConstructor
@Getter
public class Installment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;

    private Integer number;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    private BigDecimal principal;
    private BigDecimal interest;

    public Installment(LocalDate date, Loan loan, Integer number,
                       Currency currency, BigDecimal principal, BigDecimal interest) {
        this.date = date;
        this.loan = loan;
        this.number = number;
        this.currency = currency;
        this.principal = principal;
        this.interest = interest;
    }

}
