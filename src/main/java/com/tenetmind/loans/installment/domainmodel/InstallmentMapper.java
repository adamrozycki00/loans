package com.tenetmind.loans.installment.domainmodel;

import com.tenetmind.loans.currency.domainmodel.CurrencyMapper;
import com.tenetmind.loans.loan.controller.LoanNotFoundException;
import com.tenetmind.loans.loan.domainmodel.LoanMapper;
import com.tenetmind.loans.loan.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InstallmentMapper {

    @Autowired
    private LoanService loanService;

    @Autowired
    private CurrencyMapper currencyMapper;

    public Installment mapToNewEntity(final InstallmentDto dto) throws LoanNotFoundException {
        return new Installment(
                dto.getDate(),
                loanService.findById(dto.getLoanId()).orElseThrow(LoanNotFoundException::new),
                dto.getNumber(),
                dto.getPrincipal(),
                dto.getInterest());
    }

    public Installment mapToExistingEntity(final InstallmentDto dto) throws LoanNotFoundException {
        return new Installment(
                dto.getId(),
                dto.getDate(),
                loanService.findById(dto.getLoanId()).orElseThrow(LoanNotFoundException::new),
                dto.getNumber(),
                currencyMapper.mapToExistingEntity(dto.getCurrencyDto()),
                dto.getPrincipal(),
                dto.getInterest());
    }

    public InstallmentDto mapToDto(final Installment entity) {
        return new InstallmentDto(
                entity.getId(),
                entity.getDate(),
                entity.getLoan().getId(),
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

    public List<Installment> mapToEntityList (final List<InstallmentDto> installmentDtos){
        List<Installment> resultList = new ArrayList<>();
        try {
            for (InstallmentDto installmentDto : installmentDtos) {
                Installment existingEntity = mapToExistingEntity(installmentDto);
                resultList.add(existingEntity);
            }
        } catch (LoanNotFoundException e) {
            e.printStackTrace();
        }
        return resultList;
    }

}
