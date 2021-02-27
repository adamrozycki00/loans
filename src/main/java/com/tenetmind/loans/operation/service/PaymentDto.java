package com.tenetmind.loans.operation.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentDto {

    private LocalDate date;
    private Long loanId;
    private String currencyName;
    private BigDecimal amount;

    public PaymentDto(LocalDate date, Long loanId) {
        this.date = date;
        this.loanId = loanId;
        this.currencyName = "";
        this.amount = BigDecimal.ZERO;
    }

}
