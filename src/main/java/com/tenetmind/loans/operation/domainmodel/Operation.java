package com.tenetmind.loans.operation.domainmodel;

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
    private Loan loan;
    private String type;
    private BigDecimal amount;

}
