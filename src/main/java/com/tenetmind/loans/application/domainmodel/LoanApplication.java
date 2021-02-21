package com.tenetmind.loans.application.domainmodel;

import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.customer.domainmodel.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    private BigDecimal amount;
    private Integer period;
    private BigDecimal marginRate;
    private String status;

    public LoanApplication(LocalDateTime date, Customer customer, Currency currency,
                           BigDecimal amount, Integer period, BigDecimal marginRate,
                           String status) {
        this.date = date;
        this.customer = customer;
        this.currency = currency;
        this.amount = amount;
        this.period = period;
        this.marginRate = marginRate;
        this.status = status;
    }

}
