package com.tenetmind.loans.loan.domainmodel;

import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.customer.domainmodel.Customer;
import com.tenetmind.loans.installment.domainmodel.Installment;
import com.tenetmind.loans.loanapplication.domainmodel.LoanApplication;
import com.tenetmind.loans.operation.domainmodel.Operation;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "loans")
@NoArgsConstructor
@Getter
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    @OneToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "application_id")
    private LoanApplication application;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    private BigDecimal amount;
    private Integer period;
    private BigDecimal baseRate;
    private BigDecimal marginRate;
    private BigDecimal balance;
    private Integer numberOfInstallmentsPaid;
    private String status;

    @OneToMany(
            targetEntity = Installment.class,
            mappedBy = "loan",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Installment> schedule;

    @OneToMany(
            targetEntity = Operation.class,
            mappedBy = "loan",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Operation> operations;

//    public Loan(LocalDateTime date, Customer customer, Currency currency,
//                BigDecimal amount, Integer period, BigDecimal baseRate,
//                BigDecimal marginRate) {
//        this.date = date;
//        this.customer = customer;
//        this.currency = currency;
//        this.amount = amount;
//        this.period = period;
//        this.baseRate = baseRate;
//        this.marginRate = marginRate;
//        this.balance = amount;
//        this.numberOfInstallmentsPaid = 0;
//        this.status = "New";
//        this.schedule = new ArrayList<>();
//        this.operations = new ArrayList<>();
//    }

    public Loan(LocalDateTime date, LoanApplication application, BigDecimal baseRate) {
        this.date = date;
        this.application = application;
        this.customer = application.getCustomer();
        this.currency = application.getCurrency();
        this.amount = application.getAmount();
        this.period = application.getPeriod();
        this.baseRate = baseRate;
        this.marginRate = application.getMarginRate();
        this.balance = amount;
        this.numberOfInstallmentsPaid = 0;
        this.status = "New";
        this.schedule = new ArrayList<>();
        this.operations = new ArrayList<>();
    }

    public void setApplication(LoanApplication application) {
        this.application = application;
    }

}
