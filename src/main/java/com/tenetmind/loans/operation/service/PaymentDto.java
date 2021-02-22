package com.tenetmind.loans.operation.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class PaymentDto {

    private final LocalDate date;
    private final Long loanId;
    private final String currencyName;
    private final BigDecimal amount;

}
