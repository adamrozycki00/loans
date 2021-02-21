package com.tenetmind.loans.loan.domainmodel;

import com.tenetmind.loans.currency.domainmodel.CurrencyMapper;
import com.tenetmind.loans.customer.domainmodel.CustomerMapper;
import com.tenetmind.loans.installment.domainmodel.InstallmentMapper;
import com.tenetmind.loans.loanapplication.domainmodel.LoanApplicationMapper;
import com.tenetmind.loans.operation.domainmodel.OperationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoanMapper {

    @Autowired
    private LoanApplicationMapper applicationMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CurrencyMapper currencyMapper;

    @Autowired
    private InstallmentMapper installmentMapper;

    @Autowired
    private OperationMapper operationMapper;

    public Loan mapToEntity(final LoanDto dto) {
        return new Loan(
                dto.getDate(),
                applicationMapper.mapToEntity(dto.getApplicationDto()),
                dto.getBaseRate());
    }

    public LoanDto mapToDto(final Loan entity) {
        return new LoanDto(
                entity.getId(),
                entity.getDate(),
                applicationMapper.mapToDto(entity.getApplication()),
                customerMapper.mapToDto(entity.getCustomer()),
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

    public List<LoanDto> mapToDtoList(List<Loan> loans) {
        return loans.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
