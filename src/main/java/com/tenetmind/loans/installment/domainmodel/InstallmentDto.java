package com.tenetmind.loans.installment.domainmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

@AllArgsConstructor
@Getter
public class InstallmentDto {

    private final Long id;
    private final LocalDate date;
    private final Integer number;
    private final Currency currency;
    private final BigDecimal principal;
    private final BigDecimal interest;

}
