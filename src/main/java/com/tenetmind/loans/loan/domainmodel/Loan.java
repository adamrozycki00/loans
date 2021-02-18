package com.tenetmind.loans.loan.domainmodel;

import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.installment.domainmodel.Installment;
import com.tenetmind.loans.operation.domainmodel.Operation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "loans")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;
    private Currency currency;
    private BigDecimal amount;
    private Integer period;
    private BigDecimal baseRate;
    private BigDecimal marginRate;
    private BigDecimal balance;
    private Integer numberOfInstallmentsPaid;
    private String status;
    private List<Installment> schedule;
    private List<Operation> operations;

}
