package com.tenetmind.loans.loan.domainmodel;

import com.tenetmind.loans.currency.domainmodel.CurrencyDto;
import com.tenetmind.loans.customer.domainmodel.CustomerDto;
import com.tenetmind.loans.installment.domainmodel.InstallmentDto;
import com.tenetmind.loans.application.domainmodel.LoanApplicationDto;
import com.tenetmind.loans.operation.domainmodel.OperationDto;
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
    private final LoanApplicationDto applicationDto;
    private final CustomerDto customerDto;
    private final CurrencyDto currencyDto;
    private final BigDecimal amount;
    private final Integer period;
    private final BigDecimal baseRate;
    private final BigDecimal marginRate;
    private final BigDecimal balance;
    private final BigDecimal amountToPay;
    private final Integer numberOfInstallmentsPaid;
    private final String status;
    private final List<InstallmentDto> scheduleDto;
    private final List<OperationDto> operationDtos;

}
