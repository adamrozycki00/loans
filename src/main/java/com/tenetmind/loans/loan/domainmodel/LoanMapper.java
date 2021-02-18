package com.tenetmind.loans.loan.domainmodel;

import org.springframework.stereotype.Component;

@Component
public class LoanMapper {

    public Loan mapToEntity(final LoanDto dto) {
        return new Loan(
                dto.getId(),
                dto.getDate(),
                dto.getCurrency(),
                dto.getAmount(),
                dto.getPeriod(),
                dto.getBaseRate(),
                dto.getMarginRate(),
                dto.getBalance(),
                dto.getNumberOfInstallmentsPaid(),
                dto.getStatus(),
                dto.getSchedule(),
                dto.getOperations());
    }

    public LoanDto mapToDto(final Loan entity) {
        return new LoanDto(
                entity.getId(),
                entity.getDate(),
                entity.getCurrency(),
                entity.getAmount(),
                entity.getPeriod(),
                entity.getBaseRate(),
                entity.getMarginRate(),
                entity.getBalance(),
                entity.getNumberOfInstallmentsPaid(),
                entity.getStatus(),
                entity.getSchedule(),
                entity.getOperations());
    }

}
