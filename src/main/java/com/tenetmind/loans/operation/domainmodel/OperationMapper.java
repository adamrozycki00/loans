package com.tenetmind.loans.operation.domainmodel;

import org.springframework.stereotype.Component;

@Component
public class OperationMapper {

    public Operation mapToEntity(final OperationDto dto) {
        return new Operation(
                dto.getId(),
                dto.getDate(),
                dto.getLoan(),
                dto.getType(),
                dto.getAmount());
    }

    public OperationDto mapToDto(final Operation entity) {
        return new OperationDto(
                entity.getId(),
                entity.getDate(),
                entity.getLoan(),
                entity.getType(),
                entity.getAmount());
    }

}
