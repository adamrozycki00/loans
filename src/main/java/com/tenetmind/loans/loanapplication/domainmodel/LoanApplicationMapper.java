package com.tenetmind.loans.loanapplication.domainmodel;

import com.tenetmind.loans.currency.domainmodel.CurrencyMapper;
import com.tenetmind.loans.loan.domainmodel.LoanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoanApplicationMapper {

    @Autowired
    private CurrencyMapper currencyMapper;

    @Autowired
    private LoanMapper loanMapper;

    public LoanApplication mapToEntity(final LoanApplicationDto dto) {
        return new LoanApplication(
                dto.getId(),
                dto.getDate(),
                currencyMapper.mapToEntity(dto.getCurrencyDto()),
                dto.getAmount(),
                dto.getPeriod(),
                dto.getMarginRate(),
                dto.getStatus(),
                loanMapper.mapToEntity(dto.getLoanDto()));
    }

    public LoanApplicationDto mapToDto(final LoanApplication entity) {
        return new LoanApplicationDto(
                entity.getId(),
                entity.getDate(),
                currencyMapper.mapToDto(entity.getCurrency()),
                entity.getAmount(),
                entity.getPeriod(),
                entity.getMarginRate(),
                entity.getStatus(),
                loanMapper.mapToDto(entity.getLoan()));
    }

}
