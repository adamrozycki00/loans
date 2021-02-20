package com.tenetmind.loans.loanapplication.domainmodel;

import com.tenetmind.loans.currency.domainmodel.CurrencyMapper;
import com.tenetmind.loans.customer.domainmodel.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoanApplicationMapper {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CurrencyMapper currencyMapper;

    public LoanApplication mapToEntity(final LoanApplicationDto dto) {
        return new LoanApplication(
                dto.getId(),
                dto.getDate(),
                userMapper.mapToEntity(dto.getUserDto()),
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
                userMapper.mapToDto(entity.getUser()),
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
