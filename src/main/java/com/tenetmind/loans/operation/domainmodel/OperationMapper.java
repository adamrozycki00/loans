package com.tenetmind.loans.operation.domainmodel;

import com.tenetmind.loans.currency.domainmodel.CurrencyMapper;
import com.tenetmind.loans.installment.domainmodel.Installment;
import com.tenetmind.loans.installment.domainmodel.InstallmentDto;
import com.tenetmind.loans.loan.controller.LoanNotFoundException;
import com.tenetmind.loans.loan.domainmodel.LoanMapper;
import com.tenetmind.loans.loan.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OperationMapper {

    @Autowired
    private LoanService loanService;

    @Autowired
    private CurrencyMapper currencyMapper;

    public Operation mapToNewEntity(final OperationDto dto) throws LoanNotFoundException {
        return new Operation(
                dto.getDate(),
                loanService.findById(dto.getLoanId()).orElseThrow(LoanNotFoundException::new),
                dto.getType(),
                currencyMapper.mapToExistingEntity(dto.getCurrencyDto()),
                dto.getAmount(),
                dto.getPlnAmount());
    }

    public Operation mapToExistingEntity(final OperationDto dto) throws LoanNotFoundException {
        return new Operation(
                dto.getId(),
                dto.getDate(),
                loanService.findById(dto.getLoanId()).orElseThrow(LoanNotFoundException::new),
                dto.getType(),
                currencyMapper.mapToExistingEntity(dto.getCurrencyDto()),
                dto.getAmount(),
                dto.getPlnAmount());
    }

    public OperationDto mapToDto(final Operation entity) {
        return new OperationDto(
                entity.getId(),
                entity.getDate(),
                entity.getLoan().getId(),
                entity.getType(),
                currencyMapper.mapToDto(entity.getCurrency()),
                entity.getAmount(),
                entity.getPlnAmount());
    }

    public List<OperationDto> mapToDtoList(final List<Operation> operations) {
        return operations.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<Operation> mapToEntityList(final List<OperationDto> operationDtos) {
        List<Operation> resultList = new ArrayList<>();
        try {
            for (OperationDto operationDto : operationDtos) {
                Operation existingEntity = mapToExistingEntity(operationDto);
                resultList.add(existingEntity);
            }
        } catch (LoanNotFoundException e) {
            e.printStackTrace();
        }
        return resultList;
    }

}
