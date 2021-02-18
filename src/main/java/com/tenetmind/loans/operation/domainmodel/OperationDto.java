package com.tenetmind.loans.operation.domainmodel;

import com.tenetmind.loans.loan.domainmodel.Loan;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class OperationDto {

    private Long id;
    private LocalDate date;
    private Loan loan;
    private String type;
    private BigDecimal amount;

}
