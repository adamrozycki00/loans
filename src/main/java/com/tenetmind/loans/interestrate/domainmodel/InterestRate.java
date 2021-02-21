package com.tenetmind.loans.interestrate.domainmodel;

import com.tenetmind.loans.currency.domainmodel.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "interest_rates")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InterestRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    private BigDecimal rate;

    public InterestRate(String name, LocalDate date, Currency currency, BigDecimal rate) {
        this.name = name;
        this.date = date;
        this.currency = currency;
        this.rate = rate;
    }

}
