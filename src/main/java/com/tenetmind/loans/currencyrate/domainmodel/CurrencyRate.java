package com.tenetmind.loans.currencyrate.domainmodel;

import com.tenetmind.loans.currency.domainmodel.Currency;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "currency_rates")
@NoArgsConstructor
@Getter
public class CurrencyRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    private BigDecimal rate;

    public CurrencyRate(LocalDate date, Currency currency, BigDecimal rate) {
        this.date = date;
        this.currency = currency;
        this.rate = rate;
    }

}
