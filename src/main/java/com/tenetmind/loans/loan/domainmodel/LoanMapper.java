package com.tenetmind.loans.loan.domainmodel;

import com.tenetmind.loans.currency.domainmodel.CurrencyMapper;
import com.tenetmind.loans.installment.domainmodel.InstallmentMapper;
import com.tenetmind.loans.operation.domainmodel.OperationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoanMapper {

    @Autowired
    private CurrencyMapper currencyMapper;

    @Autowired
    private InstallmentMapper installmentMapper;

    @Autowired
    private OperationMapper operationMapper;

    public Loan mapToEntity(final LoanDto dto) {
        return new Loan(
                dto.getId(),
                dto.getDate(),
                currencyMapper.mapToEntity(dto.getCurrencyDto()),
                dto.getAmount(),
                dto.getPeriod(),
                dto.getBaseRate(),
                dto.getMarginRate(),
                dto.getBalance(),
                dto.getNumberOfInstallmentsPaid(),
                dto.getStatus(),
                installmentMapper.mapToEntityList(dto.getScheduleDto()),
                operationMapper.mapToEntityList(dto.getOperationDtos()));
    }

    public LoanDto mapToDto(final Loan entity) {
        return new LoanDto(
                entity.getId(),
                entity.getDate(),
                currencyMapper.mapToDto(entity.getCurrency()),
                entity.getAmount(),
                entity.getPeriod(),
                entity.getBaseRate(),
                entity.getMarginRate(),
                entity.getBalance(),
                entity.getNumberOfInstallmentsPaid(),
                entity.getStatus(),
                installmentMapper.mapToDtoList(entity.getSchedule()),
                operationMapper.mapToDtoList(entity.getOperations()));
    }

}
