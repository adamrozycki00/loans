package com.tenetmind.loans.interestrate.domainmodel;

import org.springframework.stereotype.Component;

@Component
public class InterestRateMapper {

    public InterestRate mapToEntity(final InterestRateDto dto) {
        return new InterestRate(
                dto.getId(),
                dto.getName(),
                dto.getDate(),
                dto.getCurrency(),
                dto.getRate());
    }

    public InterestRateDto mapToDto(final InterestRate entity) {
        return new InterestRateDto(
                entity.getId(),
                entity.getName(),
                entity.getDate(),
                entity.getCurrency(),
                entity.getRate());
    }

}
