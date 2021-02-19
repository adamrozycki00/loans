package com.tenetmind.loans.currency.domainmodel;

import org.springframework.stereotype.Component;

@Component
public class CurrencyMapper {

    public Currency mapToEntity(final CurrencyDto dto) {
        return new Currency(
                dto.getId(),
                dto.getName());
    }

    public CurrencyDto mapToDto(final Currency entity) {
        return new CurrencyDto(
                entity.getId(),
                entity.getName());
    }

}
