package com.tenetmind.loans.application.domainmodel;

import com.tenetmind.loans.currency.domainmodel.CurrencyDto;
import com.tenetmind.loans.customer.domainmodel.CustomerDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class LoanApplicationDto {

    private final Long id;
    private final LocalDateTime date;
    private final CustomerDto customerDto;
    private final CurrencyDto currencyDto;
    private final BigDecimal amount;
    private final Integer period;
    private final BigDecimal marginRate;
    private final String status;

}
