package com.tenetmind.loans.currencyrate.domainmodel;

import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.config.BigDecimalConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "currency_rates")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CurrencyRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @Convert(converter = BigDecimalConverter.class)
    private BigDecimal rate;

    public CurrencyRate(String name, LocalDate date, Currency currency, BigDecimal rate) {
        this.name = name;
        this.date = date;
        this.currency = currency;
        this.rate = rate;
    }

}
