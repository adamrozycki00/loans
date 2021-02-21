package com.tenetmind.loans.installment.domainmodel;

import com.tenetmind.loans.currency.domainmodel.CurrencyMapper;
import com.tenetmind.loans.loan.domainmodel.LoanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InstallmentMapper {

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private CurrencyMapper currencyMapper;

    public Installment mapToEntity(final InstallmentDto dto) {
        return new Installment(
                dto.getDate(),
                loanMapper.mapToEntity(dto.getLoanDto()),
                dto.getNumber(),
                dto.getPrincipal(),
                dto.getInterest());
    }

    public InstallmentDto mapToDto(final Installment entity) {
        return new InstallmentDto(
                entity.getId(),
                entity.getDate(),
                loanMapper.mapToDto(entity.getLoan()),
                entity.getNumber(),
                currencyMapper.mapToDto(entity.getCurrency()),
                entity.getPrincipal(),
                entity.getInterest());
    }

    public List<InstallmentDto> mapToDtoList(final List<Installment> installments) {
        return installments.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<Installment> mapToEntityList(final List<InstallmentDto> installmentDtos) {
        return installmentDtos.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());
    }

}
