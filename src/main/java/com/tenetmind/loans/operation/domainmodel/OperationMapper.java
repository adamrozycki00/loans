package com.tenetmind.loans.operation.domainmodel;

import com.tenetmind.loans.loan.domainmodel.LoanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OperationMapper {

    @Autowired
    private LoanMapper loanMapper;

    public Operation mapToEntity(final OperationDto dto) {
        return new Operation(
                dto.getId(),
                dto.getDate(),
                loanMapper.mapToEntity(dto.getLoanDto()),
                dto.getType(),
                dto.getAmount());
    }

    public OperationDto mapToDto(final Operation entity) {
        return new OperationDto(
                entity.getId(),
                entity.getDate(),
                loanMapper.mapToDto(entity.getLoan()),
                entity.getType(),
                entity.getAmount());
    }

    public List<OperationDto> mapToDtoList(final List<Operation> operations) {
        return operations.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<Operation> mapToEntityList(final List<OperationDto> operationDtos) {
        return operationDtos.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());
    }

}
