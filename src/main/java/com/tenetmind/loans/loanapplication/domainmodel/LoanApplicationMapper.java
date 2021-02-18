package com.tenetmind.loans.loanapplication.domainmodel;

import org.springframework.stereotype.Component;

@Component
public class LoanApplicationMapper {

    public LoanApplication mapToEntity(final LoanApplicationDto dto) {
        return new LoanApplication(
                dto.getId(),
                dto.getDate(),
                dto.getCurrency(),
                dto.getAmount(),
                dto.getPeriod(),
                dto.getMarginRate(),
                dto.getStatus(),
                dto.getLoan());
    }

    public LoanApplicationDto mapToDto(final LoanApplication entity) {
        return new LoanApplicationDto(
                entity.getId(),
                entity.getDate(),
                entity.getCurrency(),
                entity.getAmount(),
                entity.getPeriod(),
                entity.getMarginRate(),
                entity.getStatus(),
                entity.getLoan());
    }

}
