package com.tenetmind.loans.installment.domainmodel;

import org.springframework.stereotype.Component;

@Component
public class InstallmentMapper {

    public Installment mapToEntity(final InstallmentDto dto) {
        return new Installment(
                dto.getId(),
                dto.getDate(),
                dto.getNumber(),
                dto.getCurrency(),
                dto.getPrincipal(),
                dto.getInterest());
    }

    public InstallmentDto mapToDto(final Installment entity) {
        return new InstallmentDto(
                entity.getId(),
                entity.getDate(),
                entity.getNumber(),
                entity.getCurrency(),
                entity.getPrincipal(),
                entity.getInterest());
    }

}
