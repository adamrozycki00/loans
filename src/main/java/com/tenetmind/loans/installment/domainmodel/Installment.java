package com.tenetmind.loans.installment.domainmodel;

import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.loan.domainmodel.Loan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "installments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
                       BigDecimal principal, BigDecimal interest) {
        this.date = date;
        this.loan = loan;
        this.number = number;
        this.currency = loan.getCurrency();
        this.principal = principal;
        this.interest = interest;
    }

}
