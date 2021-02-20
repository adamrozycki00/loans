package com.tenetmind.loans.loanapplication.domainmodel;

import com.tenetmind.loans.currency.domainmodel.CurrencyMapper;
import com.tenetmind.loans.customer.domainmodel.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoanApplicationMapper {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CurrencyMapper currencyMapper;

    public LoanApplication mapToEntity(final LoanApplicationDto dto) {
        return new LoanApplication(
                dto.getDate(),
                customerMapper.mapToEntity(dto.getCustomerDto()),
                currencyMapper.mapToEntity(dto.getCurrencyDto()),
                dto.getAmount(),
                dto.getPeriod(),
                dto.getMarginRate(),
                dto.getStatus());
    }

    public LoanApplicationDto mapToDto(final LoanApplication entity) {
        return new LoanApplicationDto(
                entity.getId(),
                entity.getDate(),
                customerMapper.mapToDto(entity.getCustomer()),
                currencyMapper.mapToDto(entity.getCurrency()),
                entity.getAmount(),
                entity.getPeriod(),
                entity.getMarginRate(),
                entity.getStatus());
    }

    public List<LoanApplicationDto> mapToDtoList(final List<LoanApplication> loanApplications) {
        return loanApplications.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

}
