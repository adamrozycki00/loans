package com.tenetmind.loans.loan.domainmodel;

import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.installment.domainmodel.Installment;
import com.tenetmind.loans.operation.domainmodel.Operation;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class LoanDto {

    private final Long id;
    private final LocalDateTime date;
    private final Currency currency;
    private final BigDecimal amount;
    private final Integer period;
    private final BigDecimal baseRate;
    private final BigDecimal marginRate;
    private final BigDecimal balance;
    private final Integer numberOfInstallmentsPaid;
    private final String status;
    private final List<Installment> schedule;
    private final List<Operation> operations;

}
